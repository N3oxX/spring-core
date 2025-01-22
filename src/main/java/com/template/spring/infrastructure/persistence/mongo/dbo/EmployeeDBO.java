package com.template.spring.infrastructure.persistence.mongo.dbo;


import com.template.spring.domain.model.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.Entity;

import javax.persistence.Table;



@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeDBO extends BaseEntity {

    private String name;

    private String email;

    private String phone;
}