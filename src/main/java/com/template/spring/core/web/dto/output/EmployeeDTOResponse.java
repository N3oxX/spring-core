package com.template.spring.core.web.dto.output;

import com.template.spring.common.util.Sensitive;
import lombok.*;

@Builder
@AllArgsConstructor
@Data
public class EmployeeDTOResponse {

    private final String id;

    private final String name;

    @Sensitive
    private final String email;
    @Sensitive
    private final String phone;
}