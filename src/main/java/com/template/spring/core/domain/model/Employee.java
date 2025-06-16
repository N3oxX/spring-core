package com.template.spring.core.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Value
public class Employee extends BaseEntity {

    String name;

    String email;

    String phone;

    Integer age;
}
