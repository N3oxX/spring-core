package com.template.spring.core.web.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;


@Getter
@Setter
@Jacksonized
public class PaginatedDto<D> {

    private int pageSize;

    private int currentPage;

    private Order order;

    private D searchFields;


    @Getter
    @Setter
    @Jacksonized
    @AllArgsConstructor
    public static class Order {

        private String column;

        private String orderType;
    }

}
