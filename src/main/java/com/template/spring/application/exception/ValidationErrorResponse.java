package com.template.spring.application.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ValidationErrorResponse extends ErrorResponse {

    private List<FieldError> fieldErrors;


    @Getter
    @Setter
    public static class FieldError {
        private String field;
        private String message;

    }
}
