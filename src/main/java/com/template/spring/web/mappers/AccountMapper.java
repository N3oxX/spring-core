package com.template.spring.web.mappers;

import com.template.spring.web.responses.AccountDtoResponse;
import com.template.spring.core.domain.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

  public static AccountDtoResponse mapToResource(Account account) {
    return new AccountDtoResponse(account.getNumber(), account.getBalance().longValue() * 100);
  }

}
