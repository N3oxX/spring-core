package com.template.spring.infrastructure.persistence.mongo.dbo;

import java.math.BigDecimal;

import com.template.spring.domain.model.Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class is necessary to keep {@link Account}
 * dependency-free from the Spring Data framework and or any other persistence-related concerns.
 */
@Document(collection = "accounts")
@Getter
@Setter
public class AccountDBO {

  @Id
  private String number;
  private String customerId;
  private BigDecimal balance;

  public AccountDBO(String number, String customerId, BigDecimal balance) {
    this.number = number;
    this.customerId = customerId;
    this.balance = balance;
  }

}
