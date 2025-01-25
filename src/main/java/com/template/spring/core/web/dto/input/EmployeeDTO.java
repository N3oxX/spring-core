package com.template.spring.core.web.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.template.spring.common.util.Sensitive;
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
@ToString
public class EmployeeDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @NotNull
    private String name;

    @Sensitive
    @Email(message = "Email should be valid")
    private String email;

    @Sensitive
    private String phone;


}
