package com.template.spring.web.controller;

import com.template.spring.domain.model.Account;
import com.template.spring.application.usecase.ManagementUseCase;
import com.template.spring.application.mapper.AccountMapper;
import com.template.spring.web.dto.input.AccountDTO;
import com.template.spring.web.dto.output.AccountDTOResponse;
import com.template.spring.application.exception.InsufficientFundsException;
import com.template.spring.application.exception.UnknownAccountException;
import com.template.spring.application.usecase.WithdrawFundsUseCase;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * This class represents the primary port adapter used to interact with the web layer.
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final WithdrawFundsUseCase withdrawFundsUseCase;
  private final ManagementUseCase managementUseCase;

  private final AccountMapper accountMapper;

  @PostMapping("/{accountNumber}/actions/withdraw")
  public AccountDTOResponse withdrawFunds(@PathVariable long accountNumber, @RequestBody Long amountInCents)
      throws UnknownAccountException, InsufficientFundsException {
    Account account = withdrawFundsUseCase.withdrawFunds(accountNumber,
        BigDecimal.valueOf(amountInCents / 100));
    return accountMapper.DTOToResponse(account);
  }

  @PostMapping("/actions/create")
  public AccountDTOResponse createAccount(@RequestBody AccountDTO accountDTO) {
    Account account = managementUseCase.createAccount(accountDTO);
    return accountMapper.DTOToResponse(account);
  }

  @GetMapping("/actions/get-all")
  public List<AccountDTOResponse> getAllAccounts() {
    List<Account> accounts = managementUseCase.getAll();
    return accounts.stream().map(accountMapper::DTOToResponse).collect(Collectors.toList());
  }


}
