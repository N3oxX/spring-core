package com.template.spring.domain.model;

import com.template.spring.application.exception.InsufficientFundsException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * This class represents the domain model for an account.
 */
@Builder
@Getter
@AllArgsConstructor
public class Account {

  private final long number;
  private final String customerId;
  private BigDecimal balance;


  public void updateBalance(BigDecimal amount) throws InsufficientFundsException {
    if(balance.add(amount).compareTo(BigDecimal.ZERO) < 0) {
      throw new InsufficientFundsException("Insufficient funds");
    }
    balance = balance.add(amount);
  }

}
