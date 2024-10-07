package com.template.spring.web.controllers;

import com.template.spring.core.domain.model.Account;
import com.template.spring.core.usecases.ManagementUseCase;
import com.template.spring.web.mappers.AccountMapper;
import com.template.spring.web.responses.AccountDtoResponse;
import com.template.spring.core.exceptions.InsufficientFundsException;
import com.template.spring.core.exceptions.UnknownAccountException;
import com.template.spring.core.usecases.WithdrawFundsUseCase;
import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class represents the primary port adapter used to interact with the web layer.
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final WithdrawFundsUseCase withdrawFundsUseCase;
  private final ManagementUseCase managementUseCase;

  @PostMapping("/{accountNumber}/actions/withdraw")
  public AccountDtoResponse withdrawFunds(@PathVariable long accountNumber, @RequestBody Long amountInCents)
      throws UnknownAccountException, InsufficientFundsException {
    Account account = withdrawFundsUseCase.withdrawFunds(accountNumber,
        BigDecimal.valueOf(amountInCents / 100));
    return AccountMapper.mapToResource(account);
  }

  @PostMapping("/{accountNumber}/actions/create")
  public AccountDtoResponse createAccount(@PathVariable String accountNumber, @RequestBody Long amountInCents) {
    Account account = managementUseCase.createAccount(accountNumber,
            BigDecimal.valueOf(amountInCents / 100));
    return AccountMapper.mapToResource(account);
  }


}
