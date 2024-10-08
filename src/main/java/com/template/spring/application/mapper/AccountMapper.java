package com.template.spring.application.mapper;

import com.template.spring.infrastructure.persistence.mongo.dbo.AccountDBO;
import com.template.spring.web.dto.input.AccountDTO;
import com.template.spring.web.dto.output.AccountDTOResponse;
import com.template.spring.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountMapper extends ParamMapper<Account, AccountDTO, AccountDBO>{

  AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

  @Mapping(source = "balance", target = "balanceInCents")
  @Mapping(source = "number", target = "accountNumber")
  AccountDTOResponse DTOToResponse(Account account);


}

interface ParamMapper<E, D ,B> {

  E toEntity(D dto);
  B EntityToDBO(E entity);
  E DBOToEntity(B dbo);
  D toDto(E entity);

  List<D> listToDtoList(List<E> list);
  E updateEntity(@MappingTarget E entity, D dto);

}
