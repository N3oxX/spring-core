package com.template.spring.configuration;

import com.template.spring.core.domain.AccountServices;
import com.template.spring.core.repositories.AccountRepository;
import com.template.spring.persistence.mongo.AccountMongoRepository;
import com.template.spring.persistence.mongo.AccountRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class DefaultConfiguration {

  @Bean
  public AccountServices accountServices(AccountRepository accountRepository) {
    return new AccountServices(accountRepository);
  }

  @Bean
  public AccountRepository accountRepository(AccountMongoRepository accountMongoRepository) {
    return new AccountRepositoryImpl(accountMongoRepository);
  }

}
