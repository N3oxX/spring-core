package com.template.spring.application.mapper;


import com.template.spring.crud.CrudMapper;
import com.template.spring.domain.model.Employee;
import com.template.spring.infrastructure.persistence.dbo.EmployeeDBO;
import com.template.spring.web.dto.input.EmployeeDTO;
import com.template.spring.web.dto.output.EmployeeDTOResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EmployeeMapper extends CrudMapper<Employee, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> {


}
