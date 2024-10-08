package com.template.spring.application.usecase;

import com.template.spring.domain.model.Account;
import com.template.spring.web.dto.input.AccountDTO;

import java.util.List;

/**
 * This interface represents the primary port used to interact with the domain layer.
 */
public interface ManagementUseCase {

  Account createAccount(AccountDTO accountDTO);

  List<Account> getAllAccounts();

}
