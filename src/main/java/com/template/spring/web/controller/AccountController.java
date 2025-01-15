package com.template.spring.web.controller;

import com.template.spring.application.exception.InsufficientFundsException;
import com.template.spring.application.exception.UnknownAccountException;
import com.template.spring.application.mapper.AccountMapper;
import com.template.spring.application.usecase.ManagementUseCase;
import com.template.spring.application.usecase.WithdrawFundsUseCase;
import com.template.spring.domain.model.Account;
import com.template.spring.web.dto.input.AccountDTO;
import com.template.spring.web.dto.output.AccountDTOResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the primary port adapter used to interact with the web layer.
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController implements ControllerGeneric<AccountDTO, AccountDTOResponse, String> {

    private final WithdrawFundsUseCase withdrawFundsUseCase;
    private final ManagementUseCase managementUseCase;

    private final AccountMapper accountMapper;


    @Override
    @PostMapping
    public ResponseEntity<AccountDTOResponse> create(@RequestBody AccountDTO accountDTO) {
        Account account = managementUseCase.create(accountDTO);
        URI location = URI.create("/accounts/" + account.getId());
        return ResponseEntity.created(location).body(accountMapper.DTOToResponse(account));
    }


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTOResponse> getById(@PathVariable String id) throws UnknownAccountException {
        AccountDTOResponse account = accountMapper.DTOToResponse(managementUseCase.getById(id));
        return ResponseEntity.ok(account);
    }

    @PatchMapping("/{accountNumber}/actions/withdraw/{amountInCents}")
    public AccountDTOResponse withdrawFunds(@PathVariable String accountNumber, @PathVariable Long amountInCents)
            throws UnknownAccountException, InsufficientFundsException {
        Account account = withdrawFundsUseCase.withdrawFunds(accountNumber,
                BigDecimal.valueOf(amountInCents / 100));
        return accountMapper.DTOToResponse(account);
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<AccountDTOResponse>> findAll() {
        return ResponseEntity.ok(managementUseCase.findAll().stream().map(accountMapper::DTOToResponse).collect(Collectors.toList()));
    }


    @Override
    @PutMapping("/{id}")
    public ResponseEntity<AccountDTOResponse> update(@PathVariable String id, @RequestBody AccountDTO entity) throws UnknownAccountException {
        return ResponseEntity.ok(accountMapper.DTOToResponse(managementUseCase.update(id, entity)));
    }


    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        managementUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

}
