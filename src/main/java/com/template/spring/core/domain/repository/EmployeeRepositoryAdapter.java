package com.template.spring.core.domain.repository;

import com.template.spring.common.crud.CrudRepository;
import com.template.spring.core.domain.model.Employee;


public interface EmployeeRepositoryAdapter extends CrudRepository<Employee, String> {

    //Additional repository query here

}