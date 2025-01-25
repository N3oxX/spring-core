package com.template.spring.infrastructure.persistence.dbo;


import com.template.spring.domain.model.BaseEntity;
import com.template.spring.util.Sensitive;
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