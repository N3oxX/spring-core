package com.template.spring.infrastructure.persistence.mongo;

import com.template.spring.application.exception.UnknownAccountException;
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

    private final MongoRepository accountMongoRepository;

    private final AccountMapper accountMapper;


    @Override
    public List<Account> getAll() {
        return accountMongoRepository.findAll()
                .stream()
                .map(accountMapper::DBOToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Account getById(String id) throws UnknownAccountException {
        AccountDBO accountDBO = accountMongoRepository.findById(id)
                .orElseThrow(() -> new UnknownAccountException("Account not found with id: " + id));

        return accountMapper.DBOToEntity(accountDBO);
    }

    @Override
    public Account save(Account account) {
        AccountDBO accountDBO = accountMongoRepository.save(accountMapper.EntityToDBO(account));
        return accountMapper.DBOToEntity(accountDBO);
    }

    @Override
    public Account update(String id, Account entity) throws UnknownAccountException {
        AccountDBO accountDBO = accountMongoRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new UnknownAccountException("Account not found"));
        if (accountDBO == null) {
            return null;
        }
        accountMapper.updateAccountFields(entity, accountDBO);

        return accountMapper.DBOToEntity(accountMongoRepository.save(accountDBO));

    }

    @Override
    public void delete(String id) {
        accountMongoRepository.deleteById(id);
    }


}
