package com.template.spring.core.repositories;

import com.template.spring.core.domain.model.Account;

/**
 * This interface represents the secondary port used to interact with the persistence layer.
 */
public interface AccountRepository {

  Account findByNumber(Long number);

  Account save(Account account);

}