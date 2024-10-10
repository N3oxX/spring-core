package com.template.spring.application.service;

import com.template.spring.application.mapper.AccountMapper;
import com.template.spring.application.usecase.CrudUseCase;
import com.template.spring.domain.model.Account;
import com.template.spring.application.exception.InsufficientFundsException;
import com.template.spring.application.exception.UnknownAccountException;
import com.template.spring.domain.repository.AccountRepository;
import com.template.spring.application.usecase.ManagementUseCase;
import com.template.spring.application.usecase.WithdrawFundsUseCase;
import com.template.spring.web.dto.input.AccountDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    return accountRepository.save(accountMapper.toEntity(accountDTO));
  }

  @Override
  public List<Account> getAll() {
    return accountRepository.getAll();
  }

  @Override
  public Optional<Account> getById(String id) throws UnknownAccountException {
    return accountRepository.getById(id);
  }

  @Override
  public boolean existsById(String id) {
    return accountRepository.existsById(id);
  }

  @Override
  public Account create(Account entity) {
    return accountRepository.save(entity);
  }

  @Override
  public Account update(String id, Account entity) throws UnknownAccountException {
    return accountRepository.update(id,entity);
  }

  @Override
  public void delete(String id) {
      accountRepository.delete(id);
  }
}
