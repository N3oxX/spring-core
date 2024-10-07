package com.template.spring.domain.repository;

import com.template.spring.domain.model.Account;

/**
 * This interface represents the secondary port used to interact with the persistence layer.
 */
public interface AccountRepository {

  Account findByNumber(Long number);

  Account save(Account account);

}