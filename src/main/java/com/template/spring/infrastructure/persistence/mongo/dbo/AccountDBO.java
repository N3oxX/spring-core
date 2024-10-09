package com.template.spring.infrastructure.persistence.mongo.dbo;

import java.math.BigDecimal;

import com.template.spring.domain.model.Account;
import com.template.spring.domain.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class is necessary to keep {@link Account}
 * dependency-free from the Spring Data framework and or any other persistence-related concerns.
 */
@Document(collection = "accounts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AccountDBO extends BaseEntity{

  private String number;
  private String customerId;
  private BigDecimal balance;


}
