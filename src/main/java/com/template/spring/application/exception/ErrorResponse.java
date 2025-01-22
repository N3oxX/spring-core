package com.template.spring.application.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private String timestamp;
    private String message;
    private String details;
    private int status;

    public ErrorResponse(String message, String details, int status) {
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.message = message;
        this.details = details;
        this.status = status;
    }

}
