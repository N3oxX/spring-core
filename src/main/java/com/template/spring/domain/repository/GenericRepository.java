package com.template.spring.domain.repository;


import com.template.spring.application.exception.UnknownAccountException;

import java.util.List;
import java.util.Optional;

interface GenericRepository<T,ID> {

    List<T> getAll();
    Optional<T> getById(ID id) throws UnknownAccountException;
    boolean existsById(ID id);
    T save(T entity);
    T update(ID id, T entity) throws UnknownAccountException;
    void delete(ID id);



    T findByNumber(Long number) throws UnknownAccountException;


}
