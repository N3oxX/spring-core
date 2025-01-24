package com.template.spring.application.service;


import com.template.spring.application.exception.UnknownEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.*;
import java.util.ArrayList;
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
    private final EntityManager entityManager;

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
    @SuppressWarnings("unchecked")
    public Page<T> findPaginated(T searchFields, Pageable pageable) throws IllegalAccessException {
        // Get the class type of the entity
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
        Class<B> entityClass = (Class<B>) type;

        // Build the query dynamically
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<B> criteriaQuery = cb.createQuery(entityClass);
        Root<B> root = criteriaQuery.from(entityClass);

        List<Predicate> predicates = new ArrayList<>();

        if (searchFields != null) {
            for (Field field : searchFields.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                    Object value = field.get(searchFields);
                    if (value != null) {
                        predicates.add(cb.equal(root.get(field.getName()), value));
                    }
            }
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        // Apply sorting from Pageable
        pageable.getSort();
        List<Order> orders = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            if (order.isAscending()) {
                orders.add(cb.asc(root.get(order.getProperty())));
            } else {
                orders.add(cb.desc(root.get(order.getProperty())));
            }
        });
        criteriaQuery.orderBy(orders);

        // Apply pagination
        TypedQuery<B> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Fetch the results
        List<B> resultList = query.getResultList();

        // Count total records for pagination
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(entityClass)));
        if (!predicates.isEmpty()) {
            countQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        long count = entityManager.createQuery(countQuery).getSingleResult();

        // Map the results and return the Page
        List<T> items = resultList.stream()
                .map(mapper::DBOToEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(items, pageable, count);
    }

}
