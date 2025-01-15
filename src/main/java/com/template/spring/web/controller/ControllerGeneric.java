package com.template.spring.web.controller;

import com.template.spring.application.exception.UnknownAccountException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface ControllerGeneric<T, R, I> {

    ResponseEntity<List<R>> findAll();

    ResponseEntity<R> getById(@PathVariable I id) throws UnknownAccountException;

    ResponseEntity<R> create(@RequestBody T entity);

    ResponseEntity<R> update(@PathVariable I ID, @RequestBody T entity) throws UnknownAccountException;

    ResponseEntity<Void> delete(@PathVariable I id);

}

