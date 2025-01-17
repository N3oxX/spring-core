package com.template.spring.crud;

import java.util.Optional;

public interface CrudService<T, ID, DTO, DBO> {

   // Iterable<T> getAll();
   // Optional<T> getById(ID id);
   // boolean existsById(ID id);
    DTO create(DTO entity);
   // T update(ID id, T entity);
   // void delete(ID id);
}
