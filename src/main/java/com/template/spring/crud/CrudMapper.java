package com.template.spring.crud;

public interface CrudMapper<T, DTO, DBO> {
    T toEntity(DTO dto);  // Convert DTO to Entity
    DTO toDto(T entity);  // Convert Entity to DTO
    T DBOToEntity(DBO dbo);
    DBO EntityToDBO(T entity);
    void updateFields(T source, DBO target);
}