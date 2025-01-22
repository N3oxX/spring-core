package com.template.spring.application.service;

import com.template.spring.application.mapper.EmployeeMapper;
import com.template.spring.application.usecase.ManagementUseCase;
import com.template.spring.crud.CrudServiceImpl;
import com.template.spring.domain.model.Employee;
import com.template.spring.domain.repository.EmployeeRepositoryAdapter;
import com.template.spring.infrastructure.persistence.mongo.dbo.EmployeeDBO;
import com.template.spring.infrastructure.persistence.mongo.repository.EmployeeRepository;
import com.template.spring.web.dto.input.EmployeeDTO;
import com.template.spring.web.dto.output.EmployeeDTOResponse;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends CrudServiceImpl<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> implements ManagementUseCase {

    private final EmployeeMapper mapper;
    private final EmployeeRepositoryAdapter repository;

    public EmployeeService(EmployeeRepository repository, EmployeeMapper mapper) {
        super(repository, mapper);
        this.mapper = mapper;
        this.repository = repository;
    }


}
