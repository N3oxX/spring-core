package com.template.spring.web.dto.output;

import com.template.spring.util.Sensitive;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class EmployeeDTOResponse {

    private final String id;

    private final String name;

    @Sensitive
    private final String email;
    @Sensitive
    private final String phone;
}