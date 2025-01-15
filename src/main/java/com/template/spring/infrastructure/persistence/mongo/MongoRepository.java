package com.template.spring.infrastructure.persistence.mongo;

import com.template.spring.infrastructure.persistence.mongo.dbo.AccountDBO;

import java.util.Optional;


public interface MongoRepository extends MongoGenericRepository<AccountDBO> {

    Optional<AccountDBO> findByNumber(String number);

}
