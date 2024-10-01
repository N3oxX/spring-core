package com.template.spring.persistence.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMongoRepository extends MongoRepository<AccountDocument, String> {

}
