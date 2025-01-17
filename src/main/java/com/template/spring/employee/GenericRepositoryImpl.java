package com.template.spring.employee;


import com.template.spring.crud.CrudMapper;
import com.template.spring.crud.GenericRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


/**
 * This class is a secondary port adapter used to interact with the persistence layer.
 */
@RequiredArgsConstructor
public class GenericRepositoryImpl<T, ID, DTO, DBO> implements GenericRepository<T, ID> {

    private final JpaRepository<DBO, ID> repository;
    private final CrudMapper<T, DTO, DBO> mapper;


    @Override
    public T save(T entity) {
        DBO dbo = mapper.EntityToDBO(entity);
        DBO savedDbo = repository.save(dbo);
        return mapper.DBOToEntity(savedDbo);
    }

}
