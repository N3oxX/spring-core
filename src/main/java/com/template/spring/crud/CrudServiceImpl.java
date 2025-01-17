package com.template.spring.crud;

import org.springframework.data.repository.CrudRepository;

public abstract class CrudServiceImpl<T, ID, DTO, DBO> implements CrudService<T, ID, DTO, DBO> {

    private final CrudRepository<T, ID> repository;
    private final CrudMapper<T, DTO, DBO> mapper;

    public CrudServiceImpl(CrudRepository<T, ID> repository, CrudMapper<T, DTO, DBO> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public DTO create(DTO dto) {
        T entity = mapper.toEntity(dto);  // Map DTO to Entity
        T savedEntity = repository.save(entity);  // Save entity
        return mapper.toDto(savedEntity);  // Return saved entity as DTO
    }

}
