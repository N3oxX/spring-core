package com.template.spring.infrastructure.persistence.mongo;

import com.template.spring.domain.model.Account;
import com.template.spring.domain.repository.AccountRepository;
import com.template.spring.infrastructure.persistence.mongo.dbo.AccountDBO;
import lombok.RequiredArgsConstructor;


/**
 * This class is a secondary port adapter used to interact with the persistence layer.
 */
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

  private final AccountMongoRepository accountMongoRepository;

  @Override
  public Account findByNumber(Long number) {
    AccountDBO accountDBO = accountMongoRepository.findById(String.valueOf(number))
        .orElse(null);
    if (accountDBO == null) {
      return null;
    }
    return mapToAccount(accountDBO);
  }

  @Override
  public Account save(Account account) {
    AccountDBO accountDBO = mapToAccountDocument(account);
    accountDBO = accountMongoRepository.save(accountDBO);
    return mapToAccount(accountDBO);
  }


  private Account mapToAccount(AccountDBO accountDBO) {
    return new Account(Long.parseLong(accountDBO.getNumber()), accountDBO.getCustomerId(),
        accountDBO.getBalance());
  }

  private AccountDBO mapToAccountDocument(Account account) {
    return new AccountDBO(String.valueOf(account.getNumber()), account.getCustomerId(),
        account.getBalance());
  }

}
