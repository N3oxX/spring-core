package com.template.spring.infrastructure.persistence.memory;

import com.template.spring.domain.model.Account;
import com.template.spring.domain.repository.AccountRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is a secondary port adapter used to interact with the persistence layer.
 */
public class InMemoryAccountRepository implements AccountRepository {

  private final HashMap<Long, Account> accounts = new HashMap<>();

  @Override
  public Account findByNumber(Long number) {
    return accounts.get(number);
  }

  @Override
  public Account save(Account account) {
    accounts.put(account.getNumber(), account);
    return account;
  }

  @Override
  public List<Account> getAll() {
    return new ArrayList<>(accounts.values());
  }

}
