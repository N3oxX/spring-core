package com.template.spring.web.controller;

import com.template.spring.application.mapper.EmployeeMapper;
import com.template.spring.application.service.CrudController;
import com.template.spring.application.service.EmployeeService;
import com.template.spring.domain.model.Employee;
import com.template.spring.infrastructure.persistence.dbo.EmployeeDBO;
import com.template.spring.web.dto.input.EmployeeDTO;
import com.template.spring.web.dto.output.EmployeeDTOResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController extends CrudController<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> {

    EmployeeService service;

    public EmployeeController(EmployeeService service, EmployeeMapper mapper) {
        super(service, mapper);
        this.service = service;

    }

    // Additional endpoints if needed


}
