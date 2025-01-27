package com.template.spring.core.infrastructure.persistence.repository;


import com.template.spring.common.util.Sensitive;
import com.template.spring.core.domain.model.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeDBO extends BaseEntity {

    private String name;

    @Sensitive
    private String email;

    @Sensitive
    private String phone;
}