package com.template.spring.mapper;

import com.template.spring.employee.Employee;
import org.springframework.stereotype.Component;

interface GenericMapper<E, D> {
    E toEntity(D dto);
    D toDto(E entity);
}
