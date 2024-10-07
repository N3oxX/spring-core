package com.template.spring.core.domain.service;

import com.template.spring.core.domain.model.Account;
import com.template.spring.core.exceptions.InsufficientFundsException;
import com.template.spring.core.exceptions.UnknownAccountException;
import com.template.spring.core.repositories.AccountRepository;
import com.template.spring.core.usecases.ManagementUseCase;
import com.template.spring.core.usecases.WithdrawFundsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * This class represents the primary port implementation within the domain layer.
 */
@Service
@RequiredArgsConstructor
public class AccountServices implements WithdrawFundsUseCase, ManagementUseCase {

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
