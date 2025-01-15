package com.template.spring.web.dto.input;

import com.template.spring.domain.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
@AllArgsConstructor
public class AccountDTO extends BaseEntity{

    private final long number;
    private final String customerId;
    private final String balance;

}