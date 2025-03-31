package com.template.spring.core.infrastructure.persistence.repository;


import com.template.spring.common.crud.CrudRepositoryImpl;
import com.template.spring.core.application.mapper.EmployeeMapper;
import com.template.spring.core.domain.model.Employee;
import com.template.spring.core.domain.repository.EmployeeRepositoryAdapter;
import com.template.spring.core.web.dto.input.EmployeeDTO;
import com.template.spring.core.web.dto.output.EmployeeDTOResponse;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;


@Repository
public class EmployeeRepository extends CrudRepositoryImpl<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> implements EmployeeRepositoryAdapter {


    public EmployeeRepository(EmployeeJpaRepository repository, EmployeeMapper mapper, EntityManager entityManager) {
        super(repository, mapper, entityManager);
    }


}