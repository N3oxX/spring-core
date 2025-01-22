package com.template.spring.web.dto.input;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;


@Getter
@Setter
@Jacksonized
public class EmployeePaginatedDto<D> {

    private int pageSize;

    private int currentPage;

    private Order order;

    private D searchFields;


    @Getter
    @Setter
    @Jacksonized
    public static class Order {

        private String column;

        private String orderType;
    }

}
