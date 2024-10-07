package com.template.spring.web.mapper;

import com.template.spring.web.dto.AccountDTOResponse;
import com.template.spring.domain.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

  public static AccountDTOResponse mapToResponse(Account account) {
    return new AccountDTOResponse(account.getNumber(), account.getBalance().longValue() * 100);
  }

}
