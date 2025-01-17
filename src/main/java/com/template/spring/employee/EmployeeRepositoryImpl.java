package com.template.spring.employee;



import org.springframework.stereotype.Repository;



@Repository
public class EmployeeRepositoryImpl extends GenericRepositoryImpl<Employee, String,EmployeeDTO, EmployeeDBO> {

    public EmployeeRepositoryImpl(EmployeeJpaRepository  repository, EmployeeMapper mapper) {
        super(repository, mapper);
    }
}