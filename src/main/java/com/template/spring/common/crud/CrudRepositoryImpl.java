package com.template.spring.common.crud;


import com.template.spring.core.application.exception.UnknownEntityException;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * This class is a secondary port adapter used to interact with the persistence layer.
 */
@RequiredArgsConstructor
public class CrudRepositoryImpl<T, I, D, B, R> implements CrudRepository<T, I> {

    private final JpaRepository<B, I> repository;
    private final CrudMapper<T, D, B, R> mapper;
    private final JpaSpecificationExecutor<B> specificationExecutor;

    @Override
    public T save(T entity) {
        B dbo = mapper.EntityToDBO(entity);
        B savedDBO = repository.save(dbo);
        return mapper.DBOToEntity(savedDBO);
    }

    @Override
    public T getById(I id) throws UnknownEntityException {
        B dbo = repository.findById(id)
                .orElseThrow(() -> new UnknownEntityException("Entity not found with id: " + id));
        return mapper.DBOToEntity(dbo);
    }

    @Override
    public T update(I id, T entity) throws UnknownEntityException {
        B dbo = repository.findById(id)
                .orElseThrow(() -> new UnknownEntityException("Entity not found"));

        mapper.updateDBOFromEntity(entity, dbo);

        return mapper.DBOToEntity(repository.save(dbo));

    }

    @Override
    public List<T> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::DBOToEntity)
                .collect(Collectors.toList());
    }


    @Override
    public void delete(I id) {
        repository.deleteById(id);
    }

    @Override
    public T patch(I id, Map<String, Object> updates) throws UnknownEntityException, IllegalArgumentException {
        B dbo = repository.findById(id)
                .orElseThrow(() -> new UnknownEntityException("Entity not found"));

        updates.forEach((field, value) -> {
            if ("id".equalsIgnoreCase(field)) {
                throw new IllegalArgumentException("The 'id' field cannot be updated.");
            }
            try {
                String setterName = "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);

                Method setterMethod = dbo.getClass().getMethod(setterName, value.getClass());

                setterMethod.invoke(dbo, value);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException("Failed to set field: " + field, e);
            }
        });

        B updatedDBO = repository.save(dbo);

        return mapper.DBOToEntity(updatedDBO);
    }


    @Override
    public Page<T> findPaginated(T searchFields, Pageable pageable) throws IllegalAccessException {

        Map<String, Object> nonNullFields = extractNonNullFields(searchFields);

        Specification<B> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : nonNullFields.entrySet()) {
                Path<Object> path = root.get(entry.getKey());
                Object value = entry.getValue();

                if (value instanceof String str) {
                    predicates.add(cb.like(cb.lower(path.as(String.class)), "%" + str.toLowerCase() + "%"));
                } else {
                    predicates.add(cb.equal(path, value));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<B> dbResultPage = specificationExecutor.findAll(spec, pageable);

        List<T> items = dbResultPage.stream()
                .map(mapper::DBOToEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(items, pageable, dbResultPage.getTotalElements());
    }

    private Map<String, Object> extractNonNullFields(T searchFields) throws IllegalAccessException {
        Map<String, Object> fields = new HashMap<>();

        for (Field field : searchFields.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(searchFields);
            if (value != null) {
                fields.put(field.getName(), value);
            }
        }

        return fields;
    }

}
