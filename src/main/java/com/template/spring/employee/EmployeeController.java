package com.template.spring.employee;

import com.template.spring.crud.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController extends CrudController<Employee, String, EmployeeDTO, EmployeeDBO> {

    public EmployeeController(EmployeeService service) {
        super(service);
    }
}
