package com.template.spring.web.controller;

import com.template.spring.application.exception.UnknownAccountException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface ControllerGeneric<T, ID> {

	ResponseEntity<List<T>> findAll();


	ResponseEntity<Optional<T>> getById(@PathVariable ID id) throws UnknownAccountException;

	ResponseEntity<T> create(@RequestBody T entity);

	ResponseEntity<Boolean> existsById(@PathVariable ID id);

	ResponseEntity<T> update(@PathVariable ID ID,@RequestBody T entity) throws UnknownAccountException;

	ResponseEntity<Void> delete(@PathVariable ID id);

}

