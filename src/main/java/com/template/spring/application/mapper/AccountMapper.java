package com.template.spring.application.mapper;


import com.template.spring.domain.model.Account;
import com.template.spring.infrastructure.persistence.mongo.dbo.AccountDBO;
import com.template.spring.web.dto.input.AccountDTO;
import com.template.spring.web.dto.output.AccountDTOResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper extends EntityMapper<Account, AccountDTO, AccountDBO> {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "balance", target = "balanceInCents")
    @Mapping(source = "number", target = "accountNumber")
    @Mapping(source = "id", target = "identifier")
    AccountDTOResponse DTOToResponse(Account account);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "number", source = "number")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "balance", source = "balance")
    void updateAccountFields(Account source, @MappingTarget AccountDBO target);


}

