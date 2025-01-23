package com.template.spring.infrastructure.persistence.dbo;


import com.template.spring.domain.model.BaseEntity;
import lombok.*;


import javax.persistence.Entity;

import javax.persistence.Table;



@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDBO extends BaseEntity {

    private String name;

    private String email;

    private String phone;
}