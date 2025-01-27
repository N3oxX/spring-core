package com.template.spring.core.application.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnknownEntityException extends Exception {

    private static final long serialVersionUID = 1L;


    public UnknownEntityException(String message) {
        super(message);

    }


}