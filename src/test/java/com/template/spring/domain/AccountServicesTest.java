package com.template.spring.domain;

import com.template.spring.application.service.AccountServices;
import com.template.spring.application.exception.InsufficientFundsException;
import com.template.spring.application.exception.UnknownAccountException;
import com.template.spring.domain.model.Account;
import com.template.spring.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AccountServicesTest {

    private AccountRepository accountRepository;
    private AccountServices accountServices;

    @BeforeEach
    public void setup() {
        accountRepository = Mockito.mock(AccountRepository.class);
        accountServices = new AccountServices(accountRepository);
    }

    @Test
    public void fundsAreWithdrawnWhenAccountHasSufficientBalance()
        throws UnknownAccountException, InsufficientFundsException {
        long accountNumber = 123456L;
        Account account = new Account(accountNumber, "1", BigDecimal.valueOf(50));
        when(accountRepository.findByNumber(accountNumber)).thenReturn(account);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountRepository.save(accountCaptor.capture())).thenAnswer(invocation -> accountCaptor.getValue());

        Account updatedAccount = accountServices.withdrawFunds(accountNumber, BigDecimal.TEN);

        assertEquals(BigDecimal.valueOf(40), updatedAccount.getBalance());
        Mockito.verify(accountRepository).save(accountCaptor.getValue());
    }

    @Test
    public void exceptionIsThrownWhenAccountHasInsufficientBalance() {
        long accountNumber = 123456L;
        Account account = new Account(accountNumber, "1", BigDecimal.ZERO);
        when(accountRepository.findByNumber(accountNumber)).thenReturn(account);

        assertThrows(InsufficientFundsException.class, () -> accountServices.withdrawFunds(accountNumber, BigDecimal.TEN));
    }

    @Test
    public void exceptionIsThrownWhenAccountDoesNotExist() {
        long accountNumber = 123456L;
        when(accountRepository.findByNumber(accountNumber)).thenReturn(null);

        assertThrows(UnknownAccountException.class, () -> accountServices.withdrawFunds(accountNumber, BigDecimal.TEN));
    }
}