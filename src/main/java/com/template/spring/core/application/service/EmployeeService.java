package com.template.spring.core.application.service;

import com.template.spring.common.crud.CrudServiceImpl;
import com.template.spring.core.application.mapper.EmployeeMapper;
import com.template.spring.core.application.usecase.ManagementUseCase;
import com.template.spring.core.domain.model.Employee;
import com.template.spring.core.infrastructure.persistence.repository.EmployeeDBO;
import com.template.spring.core.infrastructure.persistence.repository.EmployeeRepository;
import com.template.spring.core.web.dto.input.EmployeeDTO;
import com.template.spring.core.web.dto.output.EmployeeDTOResponse;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends CrudServiceImpl<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> implements ManagementUseCase {


    public EmployeeService(EmployeeRepository repository, EmployeeMapper mapper) {
        super(repository, mapper);
    }


}
