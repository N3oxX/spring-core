package com.template.spring.domain.repository;

import com.template.spring.crud.CrudRepository;
import com.template.spring.domain.model.Employee;


public interface EmployeeRepositoryAdapter extends CrudRepository<Employee, String> {

    //Additional repository query here

}