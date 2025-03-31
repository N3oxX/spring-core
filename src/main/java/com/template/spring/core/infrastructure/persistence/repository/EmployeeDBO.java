package com.template.spring.core.infrastructure.persistence.repository;


import com.template.spring.core.domain.model.BaseEntity;
import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeDBO extends BaseEntity {

    private String name;


    private String email;


    private String phone;
}