package com.template.spring.domain.repository;


import com.template.spring.application.UnknownAccountException;

import java.util.List;

interface GenericRepository<T, I> {

    List<T> getAll();

    T getById(I id) throws UnknownAccountException;

    T save(T entity);

    T update(I id, T entity) throws UnknownAccountException;

    void delete(I id);

}
