package com.template.spring.core.web.controller;


import com.template.spring.core.application.exception.ErrorResponse;
import com.template.spring.core.application.exception.UnknownEntityException;
import com.template.spring.core.application.exception.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GenericControllerAdvice {


    @ExceptionHandler(UnknownEntityException.class)
    public ResponseEntity<ErrorResponse> handleUnknownEmployeeException(UnknownEntityException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "The requested employee could not be found.",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "The field don't exist.",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ErrorResponse> handleIllegalAccessException(IllegalAccessException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Access to the field is not allowed.",
                HttpStatus.FORBIDDEN.value()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<ValidationErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    ValidationErrorResponse.FieldError fieldError = new ValidationErrorResponse.FieldError();
                    fieldError.setField(fieldName);
                    fieldError.setMessage(errorMessage);
                    return fieldError;
                })
                .collect(Collectors.toList());

        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage("Validation error");
        errorResponse.setDetails(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setFieldErrors(fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
