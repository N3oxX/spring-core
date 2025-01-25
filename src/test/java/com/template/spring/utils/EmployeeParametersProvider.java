package com.template.spring.utils;

import com.template.spring.core.domain.model.Employee;
import com.template.spring.core.infrastructure.persistence.repository.EmployeeDBO;
import com.template.spring.core.web.dto.input.EmployeeDTO;
import com.template.spring.core.web.dto.output.EmployeeDTOResponse;

public class EmployeeParametersProvider {

    public static final Employee EMPLOYEE1 = Employee.builder()
            .id("41fbfe88-ea44-4606-a565-858c6e3b2e9c")
            .name("David")
            .email("david@gmail.com")
            .phone("12354353")
            .build();


    public static final Employee EMPLOYEE2 = Employee.builder()
            .id("92124e84-sa14-4n34-a452-858346457457")
            .name("Ramon")
            .email("ramon12353@hotmail.com")
            .phone("68303950243")
            .build();


    public static final Employee EMPLOYEE_NULL = Employee.builder()
            .name(null)
            .email(null)
            .phone(null)
            .build();


    public static final EmployeeDBO EMPLOYEE_DBO1 = new EmployeeDBO("David", "david@gmail.com", "12354353");
    public static final EmployeeDBO EMPLOYEE_DBO2 = new EmployeeDBO("Ramon", "ramon12353@hotmail.com", "68303950243");

    public static final EmployeeDTO EMPLOYEE_DTO1 = EmployeeDTO.builder().name("David").email("david@gmail.com").phone("12354353").build();
    public static final EmployeeDTO EMPLOYEE_DTO2 = EmployeeDTO.builder().name("Ramon").email("ramon12353@hotmail.com").phone("68303950243").build();

    public static final EmployeeDTO EMPLOYEE_EMAIL_WRONG1 = EmployeeDTO.builder().name("Ramon").email("wrong email").phone("68303950243").build();
    public static final EmployeeDTO EMPLOYEE_EMAIL_WRONG2 = EmployeeDTO.builder().name("Ramon").email("i[w]@g.com").phone("68303950243").build();

    public static final EmployeeDTOResponse EMPLOYEE_RESPONSE_DTO1 = EmployeeDTOResponse.builder()
            .name(EMPLOYEE_DTO1.getName())
            .email(EMPLOYEE_DTO1.getEmail())
            .phone(EMPLOYEE_DTO1.getPhone())
            .build();

    public static final EmployeeDTOResponse EMPLOYEE_RESPONSE_DTO2 = EmployeeDTOResponse.builder()
            .name(EMPLOYEE_DTO2.getName())
            .email(EMPLOYEE_DTO2.getEmail())
            .phone(EMPLOYEE_DTO2.getPhone())
            .build();

    public static final EmployeeDTOResponse EMPLOYEE_RESPONSE_DTO_PATCH1 = EmployeeDTOResponse.builder()
            .name(EMPLOYEE_DTO1.getName() + " patched")
            .email(EMPLOYEE_DTO1.getEmail())
            .phone(EMPLOYEE_DTO1.getPhone())
            .build();

    public static final EmployeeDTOResponse EMPLOYEE_RESPONSE_DTO_PATCH2 = EmployeeDTOResponse.builder()
            .name(EMPLOYEE_DTO2.getName() + " patched")
            .email(EMPLOYEE_DTO2.getEmail())
            .phone(EMPLOYEE_DTO2.getPhone())
            .build();

}