package com.template.spring.web.dto.input;

import com.template.spring.domain.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AccountDTO extends BaseEntity{

    private final long number;
    private final String customerId;
    private final String balance;

}