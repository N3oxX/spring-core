package com.template.spring.application.usecase;

import com.template.spring.application.exception.UnknownAccountException;
import com.template.spring.domain.model.BaseEntity;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents the primary port used to interact with the domain layer.
 */
public interface CrudUseCase<T extends BaseEntity, S> {

    List<T> findAll();
    Optional<T> getById(String  id) throws UnknownAccountException;
    boolean existsById(String id);
    T create(T entity);
    T update(String id, S entity) throws UnknownAccountException;
    void delete(String id);
}
