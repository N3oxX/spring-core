package com.template.spring.application.mapper;

import com.template.spring.infrastructure.persistence.mongo.dbo.AccountDBO;
import com.template.spring.web.dto.input.AccountDTO;
import com.template.spring.web.dto.output.AccountDTOResponse;
import com.template.spring.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

  AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

  @Mapping(source = "balance", target = "balanceInCents")
  @Mapping(source = "number", target = "accountNumber")
  AccountDTOResponse DTOToResponse(Account account);

  AccountDBO AccountToAccountDBO(Account account);

  Account AccountDBOToAccount(AccountDBO accountDBO);

  Account AccountDTOToAccount(AccountDTO accountDTO);
}
