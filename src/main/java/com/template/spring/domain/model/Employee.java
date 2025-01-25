package com.template.spring.domain.model;

import com.template.spring.util.Sensitive;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString
public class Employee extends BaseEntity {

    private final String name;

    @Sensitive
    private final String email;
    @Sensitive
    private final String phone;
}
