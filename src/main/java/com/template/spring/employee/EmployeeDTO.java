package com.template.spring.employee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class EmployeeDTO {

    private String id;

    private String email;

    private String phone;

    public EmployeeDTO() {

    }
}
