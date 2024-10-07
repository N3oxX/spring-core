package com.template.spring.persistence.mongo;

import com.template.spring.core.domain.model.Account;
import com.template.spring.core.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * This class is a secondary port adapter used to interact with the persistence layer.
 */
@RequiredArgsConstructor
@Repository
public class AccountRepositoryImpl implements AccountRepository {

  private final AccountMongoRepository accountMongoRepository;

  @Override
  public Account findByNumber(Long number) {
    AccountDocument accountDocument = accountMongoRepository.findById(String.valueOf(number))
        .orElse(null);
    if (accountDocument == null) {
      return null;
    }
    return mapToAccount(accountDocument);
  }

  @Override
  public Account save(Account account) {
    AccountDocument accountDocument = mapToAccountDocument(account);
    accountDocument = accountMongoRepository.save(accountDocument);
    return mapToAccount(accountDocument);
  }


  private Account mapToAccount(AccountDocument accountDocument) {
    return new Account(Long.valueOf(accountDocument.getNumber()), accountDocument.getCustomerId(),
        accountDocument.getBalance());
  }

  private AccountDocument mapToAccountDocument(Account account) {
    return new AccountDocument(String.valueOf(account.getNumber()), account.getCustomerId(),
        account.getBalance());
  }

}
