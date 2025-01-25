package com.template.spring.common.crud;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface CrudMapper<T, D, B, R> {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    T DTOToEntity(D dto);

    D EntityToDTO(T entity);

    T DBOToEntity(B dbo);

    B EntityToDBO(T entity);

    R DTOToResponse(D entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateDBOFromEntity(T entity, @MappingTarget B dbo);
}