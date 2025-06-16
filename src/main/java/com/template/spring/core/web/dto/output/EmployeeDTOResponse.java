package com.template.spring.core.web.dto.output;

import lombok.Builder;

@Builder
public record EmployeeDTOResponse(String id, String name, String email, String phone) {

}