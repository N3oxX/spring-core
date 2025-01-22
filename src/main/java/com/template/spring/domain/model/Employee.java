package com.template.spring.domain.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class Employee extends BaseEntity {

    private final String name;

    private final String email;

    private final String phone;
}
