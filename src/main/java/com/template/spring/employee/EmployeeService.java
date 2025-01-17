package com.template.spring.employee;

import com.template.spring.crud.CrudServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends CrudServiceImpl<Employee, String, EmployeeDTO, EmployeeDBO> {

    public EmployeeService(EmployeeRepository repository, EmployeeMapper mapper) {
        super(repository, mapper);
    }
}
