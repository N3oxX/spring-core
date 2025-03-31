package com.template.spring.core.web.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;


@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @NotNull
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    private String phone;


}
