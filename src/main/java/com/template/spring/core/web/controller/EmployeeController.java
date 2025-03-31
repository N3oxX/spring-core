package com.template.spring.core.web.controller;

import com.template.spring.common.crud.CrudController;
import com.template.spring.core.application.mapper.EmployeeMapper;
import com.template.spring.core.application.service.EmployeeService;
import com.template.spring.core.domain.model.Employee;
import com.template.spring.core.infrastructure.persistence.repository.EmployeeDBO;
import com.template.spring.core.web.dto.input.EmployeeDTO;
import com.template.spring.core.web.dto.output.EmployeeDTOResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController extends CrudController<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> {

    public EmployeeController(EmployeeService service, @Qualifier("employeeMapperImpl") EmployeeMapper mapper) {
        super(service, mapper);

    }

    // Additional endpoints if needed


}
