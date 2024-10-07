package com.template.spring.infrastructure.persistence.mongo;

import com.template.spring.infrastructure.persistence.mongo.dbo.AccountDBO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMongoRepository extends MongoRepository<AccountDBO, String> {

}
