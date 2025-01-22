package com.template.spring.application.service;

import com.template.spring.application.InsufficientFundsException;
import com.template.spring.application.UnknownAccountException;
import com.template.spring.application.mapper.AccountMapper;
import com.template.spring.application.usecase.ManagementUseCase;
import com.template.spring.application.usecase.WithdrawFundsUseCase;
import com.template.spring.domain.model.Account;
import com.template.spring.domain.repository.AccountRepository;
import com.template.spring.web.dto.input.AccountDTO;
import lombok.RequiredArgsConstructor;

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
    public Account create(AccountDTO accountDTO) {
        return accountRepository.save(accountMapper.toEntity(accountDTO));
    }

    @Override
    public Account getById(String id) throws UnknownAccountException {
        return accountRepository.getById(id);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.getAll();
    }


    @Override
    public Account update(String id, AccountDTO accountDTO) throws UnknownAccountException {
        return accountRepository.update(id, accountMapper.toEntity(accountDTO));
    }


    @Override
    public Account withdrawFunds(String accountId, BigDecimal amount)
            throws UnknownAccountException, InsufficientFundsException {
        Account account = accountRepository.getById(accountId);
        if (account == null) {
            throw new UnknownAccountException("Account not found");
        }
        account.updateBalance(amount.negate());
        return accountRepository.save(account);
    }


    @Override
    public void delete(String id) {
        accountRepository.delete(id);
    }
}
