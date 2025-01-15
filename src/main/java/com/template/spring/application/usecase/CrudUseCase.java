package com.template.spring.application.usecase;

import com.template.spring.application.exception.UnknownAccountException;
import com.template.spring.domain.model.BaseEntity;

import java.util.List;

/**
 * This interface represents the primary port used to interact with the domain layer.
 */
public interface CrudUseCase<T extends BaseEntity, S> {

    T create(S entity);

    T getById(String id) throws UnknownAccountException;

    List<T> findAll();

    T update(String id, S entity) throws UnknownAccountException;

    void delete(String id);
}
