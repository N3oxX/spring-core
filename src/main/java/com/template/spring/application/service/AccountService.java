package com.template.spring.application.service;

import com.template.spring.application.mapper.AccountMapper;
import com.template.spring.domain.model.Account;
import com.template.spring.application.exception.InsufficientFundsException;
import com.template.spring.application.exception.UnknownAccountException;
import com.template.spring.domain.repository.AccountRepository;
import com.template.spring.application.usecase.ManagementUseCase;
import com.template.spring.application.usecase.WithdrawFundsUseCase;
import com.template.spring.web.dto.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class represents the primary port implementation within the domain layer.
 */
@RequiredArgsConstructor
public class AccountService implements WithdrawFundsUseCase, ManagementUseCase {

  private final AccountRepository accountRepository;

  private final AccountMapper accountMapper;

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
  public Account createAccount(AccountDTO accountDTO) {
    Account account = accountRepository.save(accountMapper.AccountDTOToAccount(accountDTO));
    return accountRepository.save(account);
  }

  @Override
  public List<Account> getAllAccounts() {
    return accountRepository.getAll();
  }
}
