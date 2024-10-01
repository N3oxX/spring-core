package com.template.spring.web.mappers;

import com.template.spring.web.responses.AccountResource;
import com.template.spring.core.domain.Account;

public class AccountMapper {

  public static AccountResource mapToResource(Account account) {
    return new AccountResource(account.getNumber(), account.getBalance().longValue() * 100);
  }

}
