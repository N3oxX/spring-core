package com.template.spring.core.exceptions;



public class InsufficientFundsException extends Exception {


  private static final long serialVersionUID = 1L;

  public InsufficientFundsException(String message) {
    super(message);
  }

}
