package com.template.spring.application.usecase;

import com.template.spring.application.InsufficientFundsException;
import com.template.spring.application.UnknownAccountException;
import com.template.spring.domain.model.Account;

import java.math.BigDecimal;

/**
 * This interface represents the primary port used to interact with the domain layer.
 */
public interface WithdrawFundsUseCase {

    Account withdrawFunds(String accountId, BigDecimal amount)
            throws UnknownAccountException, InsufficientFundsException;


}
