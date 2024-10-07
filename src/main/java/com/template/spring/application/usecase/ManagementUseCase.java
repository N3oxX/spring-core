package com.template.spring.application.usecase;

import com.template.spring.domain.model.Account;

import java.math.BigDecimal;

/**
 * This interface represents the primary port used to interact with the domain layer.
 */
public interface ManagementUseCase {

  Account createAccount(String accountNumber, BigDecimal amount);

}
