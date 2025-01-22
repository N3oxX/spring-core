package com.template.spring.web.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
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
