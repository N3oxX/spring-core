package com.template.spring.infrastructure.persistence.repository;


import com.template.spring.application.mapper.EmployeeMapper;
import com.template.spring.application.service.CrudRepositoryImpl;
import com.template.spring.domain.model.Employee;
import com.template.spring.domain.repository.EmployeeRepositoryAdapter;
import com.template.spring.infrastructure.persistence.dbo.EmployeeDBO;
import com.template.spring.web.dto.input.EmployeeDTO;
import com.template.spring.web.dto.output.EmployeeDTOResponse;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;


@Repository
public class EmployeeRepository extends CrudRepositoryImpl<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> implements EmployeeRepositoryAdapter {

    private final EmployeeMapper mapper;
    private final EmployeeJpaRepository repository;


    public EmployeeRepository(EmployeeJpaRepository repository, EmployeeMapper mapper, EntityManager entityManager) {
        super(repository, mapper, entityManager);
        this.repository = repository;
        this.mapper = mapper;
    }


}