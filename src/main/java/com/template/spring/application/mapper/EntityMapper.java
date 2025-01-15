package com.template.spring.application.mapper;

interface EntityMapper<E, T, B> {

    E toEntity(T dto);

    B EntityToDBO(E entity);

    E DBOToEntity(B dbo);


}
