package com.template.spring.infrastructure.configuration;


import com.template.spring.application.service.AccountService;
import com.template.spring.domain.repository.AccountRepository;
import com.template.spring.infrastructure.persistence.mongo.AccountMongoRepository;
import com.template.spring.infrastructure.persistence.mongo.AccountRepositoryImpl;
import com.template.spring.application.mapper.AccountMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("!local")
public class DefaultConfiguration {

    @Bean
    public AccountService accountServices(AccountRepository accountRepository) {
        return new AccountService(accountRepository);
    }

    @Bean
    public AccountRepository accountRepository(AccountMongoRepository accountMongoRepository, AccountMapper accountMapper) {
        return new AccountRepositoryImpl(accountMongoRepository, accountMapper);
    }

    @Bean
    public AccountMapper accountMapper() {
        return AccountMapper.INSTANCE;
    }

}
