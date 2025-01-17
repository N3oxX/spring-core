package com.template.spring.employee;



import com.template.spring.crud.CrudMapper;
import org.springframework.stereotype.Component;


@Component
public class EmployeeMapper implements CrudMapper<Employee, EmployeeDTO, EmployeeDBO> {

    @Override
    public Employee toEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        return employee;
    }

    @Override
    public EmployeeDTO toDto(Employee entity) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        return dto;
    }

    @Override
    public Employee DBOToEntity(EmployeeDBO employeeDBO) {
        Employee employee = new Employee();
        employee.setId(employeeDBO.getId());
        employee.setPhone(employeeDBO.getPhone());
        employee.setEmail(employeeDBO.getEmail());
        return employee;
    }

    @Override
    public EmployeeDBO EntityToDBO(Employee entity) {
        EmployeeDBO dbo = new EmployeeDBO();
        dbo.setId(entity.getId());
        dbo.setEmail(entity.getEmail());
        dbo.setPhone(entity.getPhone());
        return dbo;
    }

    @Override
    public void updateFields(Employee source, EmployeeDBO target) {

    }
}

