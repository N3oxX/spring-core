package com.template.spring.infrastructure.persistence.dbo;


import com.template.spring.domain.model.BaseEntity;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class EmployeeDBO extends BaseEntity {

    private String name;

    private String email;

    private String phone;
}