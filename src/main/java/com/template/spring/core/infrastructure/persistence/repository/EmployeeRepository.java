package com.template.spring.core.infrastructure.persistence.repository;


import com.template.spring.common.crud.CrudRepositoryImpl;
import com.template.spring.core.application.mapper.EmployeeMapper;
import com.template.spring.core.domain.model.Employee;
import com.template.spring.core.domain.repository.EmployeeRepositoryAdapter;
import com.template.spring.core.web.dto.input.EmployeeDTO;
import com.template.spring.core.web.dto.output.EmployeeDTOResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


@Repository
public class EmployeeRepository extends CrudRepositoryImpl<Employee, String, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> implements EmployeeRepositoryAdapter {


    public EmployeeRepository(EmployeeJpaRepository repository, @Qualifier("employeeMapperImpl") EmployeeMapper mapper) {
        super(repository, mapper, repository);
    }


}