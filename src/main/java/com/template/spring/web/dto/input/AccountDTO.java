package com.template.spring.web.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AccountDTO {

    private final long number;
    private final String customerId;
    private final String balance;

}