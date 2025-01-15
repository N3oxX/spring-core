package com.template.spring.infrastructure.persistence.mongo.dbo;

import com.template.spring.domain.model.Account;
import com.template.spring.domain.model.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * This class is necessary to keep {@link Account}
 * dependency-free from the Spring Data framework and or any other persistence-related concerns.
 */
@Document(collection = "accounts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AccountDBO extends BaseEntity {

    private String number;
    private String customerId;
    private BigDecimal balance;


}
