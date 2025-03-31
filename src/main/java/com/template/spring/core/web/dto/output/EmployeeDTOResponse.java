package com.template.spring.core.web.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class EmployeeDTOResponse {

    private final String id;

    private final String name;

    private final String email;
    private final String phone;
}