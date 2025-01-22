package com.template.spring.web.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeDTOResponse {

    private final String id;

    private final String name;

    private final String email;

    private final String phone;
}