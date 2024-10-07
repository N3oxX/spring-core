package com.template.spring.application.usecase;

import com.template.spring.domain.model.Account;
import com.template.spring.application.exception.InsufficientFundsException;
import com.template.spring.application.exception.UnknownAccountException;

import java.math.BigDecimal;

/**
 * This interface represents the primary port used to interact with the domain layer.
 */
public interface WithdrawFundsUseCase {

  Account withdrawFunds(Long accountNumber, BigDecimal amount)
      throws UnknownAccountException, InsufficientFundsException;

}
