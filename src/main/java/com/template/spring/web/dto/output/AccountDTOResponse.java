package com.template.spring.web.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountDTOResponse {


    private final long accountNumber;
    private final long balanceInCents;
    private final String identifier;


}