package com.template.spring.core.application.mapper;


import com.template.spring.common.crud.CrudMapper;
import com.template.spring.core.domain.model.Employee;
import com.template.spring.core.infrastructure.persistence.repository.EmployeeDBO;
import com.template.spring.core.web.dto.input.EmployeeDTO;
import com.template.spring.core.web.dto.output.EmployeeDTOResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EmployeeMapper extends CrudMapper<Employee, EmployeeDTO, EmployeeDBO, EmployeeDTOResponse> {


}
