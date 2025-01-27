package com.template.spring.core.domain.model;

import com.template.spring.common.util.Sensitive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Value
public class Employee extends BaseEntity {

    String name;

    @Sensitive
    String email;
    @Sensitive
    String phone;
}
