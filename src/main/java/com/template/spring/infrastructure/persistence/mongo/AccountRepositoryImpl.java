package com.template.spring.infrastructure.persistence.mongo;

import com.template.spring.application.mapper.AccountMapper;
import com.template.spring.domain.model.Account;
import com.template.spring.domain.repository.AccountRepository;
import com.template.spring.infrastructure.persistence.mongo.dbo.AccountDBO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


/**
 * This class is a secondary port adapter used to interact with the persistence layer.
 */
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

  private final AccountMongoRepository accountMongoRepository;

  private final AccountMapper accountMapper;

  @Override
  public Account findByNumber(Long number) {
    AccountDBO accountDBO = accountMongoRepository.findById(String.valueOf(number))
        .orElse(null);
    if (accountDBO == null) {
      return null;
    }
    return accountMapper.AccountDBOToAccount(accountDBO);
  }

  @Override
  public Account save(Account account) {
    AccountDBO accountDBO = accountMapper.AccountToAccountDBO(account);
    accountDBO = accountMongoRepository.save(accountDBO);
    return accountMapper.AccountDBOToAccount(accountDBO);
  }

  @Override
  public List<Account> getAll() {

    return accountMongoRepository.findAll()
            .stream()
            .map(accountMapper::AccountDBOToAccount)
            .collect(Collectors.toList());
  }


}
