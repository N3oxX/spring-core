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
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class represents the primary port adapter used to interact with the web layer.
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController implements ControllerGeneric<AccountDTO, String> {

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

  @Override
  @GetMapping("/all")
  public ResponseEntity<List<AccountDTO>> findAll() {
    return  ResponseEntity.ok(managementUseCase.findAll().stream().map(accountMapper::toDto).collect(Collectors.toList()));
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<Optional<AccountDTO>> getById(@PathVariable String id) throws UnknownAccountException {
    Optional<AccountDTO> accountOptional = managementUseCase.getById(id).map(accountMapper::toDto);
    return ResponseEntity.ok(accountOptional);
  }

  @Override
  @GetMapping("/exist/{id}")
  public ResponseEntity<Boolean> existsById(@PathVariable String id) {
    return ResponseEntity.ok(managementUseCase.existsById(id));
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<AccountDTO> update(@PathVariable String id, @RequestBody AccountDTO entity) throws UnknownAccountException {
    return ResponseEntity.ok(accountMapper.toDto(managementUseCase.update(id, entity)));
  }

  @Override
  @PostMapping
  public ResponseEntity<AccountDTO> create(AccountDTO entity) {
    Account account = managementUseCase.createAccount(entity);
    return  ResponseEntity.ok(accountMapper.toDto(account));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    managementUseCase.delete(id);
    return ResponseEntity.noContent().build();
  }

}
