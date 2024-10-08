package com.template.spring.infrastructure.configuration;

import com.template.spring.application.mapper.AccountMapper;
import com.template.spring.application.service.AccountService;
import com.template.spring.domain.repository.AccountRepository;
import com.template.spring.infrastructure.persistence.memory.InMemoryAccountRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This configuration is used when the application is running in a local environment.
 * The secondary port adapter stores data in memory.
 */
@Configuration
@Profile("local")
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
    MongoRepositoriesAutoConfiguration.class})
public class LocalConfiguration {

  @Bean
  public AccountService accountServices(AccountRepository accountRepository, AccountMapper accountMapper) {
    return new AccountService(accountRepository, accountMapper);
  }

  @Bean
  public AccountRepository accountRepository() {
    return new InMemoryAccountRepository();
  }

  @Bean
  public AccountMapper accountMapper() {
    return AccountMapper.INSTANCE;
  }


}
