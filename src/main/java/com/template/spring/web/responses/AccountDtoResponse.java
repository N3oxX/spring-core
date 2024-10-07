package com.template.spring.web.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountDtoResponse {

    private final long accountNumber;
    private final long balanceInCents;

}