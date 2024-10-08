package com.template.spring.web.controller;

import com.template.spring.domain.model.Account;
import com.template.spring.application.usecase.ManagementUseCase;
import com.template.spring.application.mapper.AccountMapper;
import com.template.spring.web.dto.AccountDTOResponse;
import com.template.spring.application.exception.InsufficientFundsException;
import com.template.spring.application.exception.UnknownAccountException;
import com.template.spring.application.usecase.WithdrawFundsUseCase;
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

  private final AccountMapper mapper;

  @PostMapping("/{accountNumber}/actions/withdraw")
  public AccountDTOResponse withdrawFunds(@PathVariable long accountNumber, @RequestBody Long amountInCents)
      throws UnknownAccountException, InsufficientFundsException {
    Account account = withdrawFundsUseCase.withdrawFunds(accountNumber,
        BigDecimal.valueOf(amountInCents / 100));
    return mapper.DTOToResponse(account);
  }

  @PostMapping("/{accountNumber}/actions/create")
  public AccountDTOResponse createAccount(@PathVariable String accountNumber, @RequestBody Long amountInCents) {
    Account account = managementUseCase.createAccount(accountNumber,
            BigDecimal.valueOf(amountInCents / 100));
    return mapper.DTOToResponse(account);
  }


}
