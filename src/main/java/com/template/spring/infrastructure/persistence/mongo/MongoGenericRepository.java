package com.template.spring.infrastructure.persistence.mongo;

import com.template.spring.domain.model.BaseEntity;
import com.template.spring.infrastructure.persistence.mongo.dbo.AccountDBO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MongoGenericRepository<T extends BaseEntity> extends MongoRepository<T, String> {

}

