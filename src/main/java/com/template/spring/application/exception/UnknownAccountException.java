package com.template.spring.application.exception;



public class UnknownAccountException extends Exception {


  private static final long serialVersionUID = 1L;

  public UnknownAccountException(String message) {
    super(message);
  }

}
