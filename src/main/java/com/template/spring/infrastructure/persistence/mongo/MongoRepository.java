package com.template.spring.infrastructure.persistence.mongo;

import com.template.spring.infrastructure.persistence.mongo.dbo.AccountDBO;

import java.util.Optional;

/*@Repository
public interface AccountMongoRepository extends MongoRepository<AccountDBO, String> {

}*/


public interface MongoRepository extends MongoGenericRepository<AccountDBO> {

    Optional<AccountDBO> findByNumber(String number);

}
