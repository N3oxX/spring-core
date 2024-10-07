package com.template.spring.application.service;

import com.template.spring.domain.model.Account;
import com.template.spring.application.exception.InsufficientFundsException;
import com.template.spring.application.exception.UnknownAccountException;
import com.template.spring.domain.repository.AccountRepository;
import com.template.spring.application.usecase.ManagementUseCase;
import com.template.spring.application.usecase.WithdrawFundsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * This class represents the primary port implementation within the domain layer.
 */
@RequiredArgsConstructor
public class AccountService implements WithdrawFundsUseCase, ManagementUseCase {

  private final AccountRepository accountRepository;

  @Override
  public Account withdrawFunds(Long accountNumber, BigDecimal amount)
      throws UnknownAccountException, InsufficientFundsException {
    Account account = accountRepository.findByNumber(accountNumber);
    if(account == null) {
      throw new UnknownAccountException("Account not found");
    }
    account.updateBalance(amount.negate());
    return accountRepository.save(account);
  }

  @Override
  public Account createAccount(String accountNumber, BigDecimal amount) {
    Account account = accountRepository.save(Account.builder().balance(amount).customerId(accountNumber).number(Long.parseLong(accountNumber)).build());
    return accountRepository.save(account);
  }
}
