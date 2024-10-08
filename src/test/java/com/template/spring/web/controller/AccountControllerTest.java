package com.template.spring.web.controller;

import com.template.spring.domain.model.Account;
import com.template.spring.application.exception.InsufficientFundsException;
import com.template.spring.application.exception.UnknownAccountException;
import com.template.spring.application.usecase.ManagementUseCase;
import com.template.spring.application.usecase.WithdrawFundsUseCase;
import com.template.spring.application.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WithdrawFundsUseCase withdrawFundsUseCase;

    @MockBean
    private ManagementUseCase managementUseCase;

    @MockBean
    private AccountMapper mapper;

    @Test
    public void withdrawFundsReturnsAccountResourceWhenSuccessful() throws Exception {
        when(withdrawFundsUseCase.withdrawFunds(anyLong(), any(BigDecimal.class)))
                .thenReturn(new Account(123456L, "1", BigDecimal.valueOf(50)));

        mockMvc.perform(post("/accounts/123456/actions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("10"))
                .andExpect(status().isOk());
    }

    @Test
    public void withdrawFundsReturnsBadRequestWhenUnknownAccount() throws Exception {
        when(withdrawFundsUseCase.withdrawFunds(anyLong(), any(BigDecimal.class)))
                .thenThrow(new UnknownAccountException("Account not found"));

        mockMvc.perform(post("/accounts/123456/actions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void withdrawFundsReturnsBadRequestWhenInsufficientFunds() throws Exception {
        when(withdrawFundsUseCase.withdrawFunds(anyLong(), any(BigDecimal.class)))
            .thenThrow(new InsufficientFundsException("Account not found"));

        mockMvc.perform(post("/accounts/123456/actions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("10"))
            .andExpect(status().isBadRequest());
    }
}