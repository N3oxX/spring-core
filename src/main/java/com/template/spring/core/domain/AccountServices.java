package com.template.spring.core.domain;

import com.template.spring.core.exceptions.InsufficientFundsException;
import com.template.spring.core.exceptions.UnknownAccountException;
import com.template.spring.core.repositories.AccountRepository;
import com.template.spring.core.usecases.WithdrawFundsUseCase;
import java.math.BigDecimal;

/**
 * This class represents the primary port implementation within the domain layer.
 */
public class AccountServices implements WithdrawFundsUseCase {

  private final AccountRepository accountRepository;

  public AccountServices(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public Account withdrawFunds(Long accountNumber, BigDecimal amount)
      throws UnknownAccountException, InsufficientFundsException {
    //accountRepository.save(new Account(2L,"2", BigDecimal.valueOf(100L)));
    Account account = accountRepository.findByNumber(accountNumber);
    if(account == null) {
      throw new UnknownAccountException("Account not found");
    }
    account.updateBalance(amount.negate());
    return accountRepository.save(account);
  }
}
