package com.template.spring.crud;

import com.template.spring.application.exception.UnknownEntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CrudRepository<T, I> {

    T save(T entity);

    T getById(I id) throws UnknownEntityException;

    T update(I id, T entity) throws UnknownEntityException;

    List<T> getAll();

    void delete(I id);

    T patch(I id, Map<String, Object> updates) throws UnknownEntityException, IllegalArgumentException, UnknownEntityException;

    Page<T> findPaginated(T searchFields, Pageable pageable) throws IllegalAccessException;

}