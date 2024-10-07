package com.template.spring.infrastructure.persistence.mongo;

import com.template.spring.domain.model.Account;
import com.template.spring.infrastructure.persistence.mongo.dbo.AccountDBO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountRepositoryImplTest {

  @MockBean
  private AccountMongoRepository accountMongoRepository;

  private AccountRepositoryImpl accountRepository;

  @BeforeEach
  public void setup() {
    accountRepository = new AccountRepositoryImpl(accountMongoRepository);
  }

  @Test
  public void testFindByNumber() {
    long accountNumber = 123456L;
    AccountDBO accountDBO = new AccountDBO(String.valueOf(accountNumber), "1",
        BigDecimal.ZERO);
    when(accountMongoRepository.findById(String.valueOf(accountNumber))).thenReturn(
        Optional.of(accountDBO));

    Account account = accountRepository.findByNumber(accountNumber);

    assertEquals(accountNumber, account.getNumber());
  }

  @Test
  public void testFindByNumberNotFound() {
    long accountNumber = 123456L;
    when(accountMongoRepository.findById(String.valueOf(accountNumber))).thenReturn(Optional.empty());

    Account account = accountRepository.findByNumber(accountNumber);

    assertNull(account);
  }

  @Test
  public void testSave() {
    long accountNumber = 123456L;
    Account account = new Account(accountNumber, "1", BigDecimal.ZERO);
    AccountDBO accountDBO = new AccountDBO(String.valueOf(accountNumber), "1", BigDecimal.ZERO);
    when(accountMongoRepository.save(Mockito.any(AccountDBO.class))).thenReturn(
            accountDBO);

    Account savedAccount = accountRepository.save(account);

    assertEquals(accountNumber, savedAccount.getNumber());
  }
}