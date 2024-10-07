package com.template.spring.core.usecases;

import com.template.spring.core.domain.model.Account;
import com.template.spring.core.exceptions.InsufficientFundsException;
import com.template.spring.core.exceptions.UnknownAccountException;

import java.math.BigDecimal;

/**
 * This interface represents the primary port used to interact with the domain layer.
 */
public interface WithdrawFundsUseCase {

  Account withdrawFunds(Long accountNumber, BigDecimal amount)
      throws UnknownAccountException, InsufficientFundsException;

}
