package com.template.spring.core.web.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaginatedDTO<D> {

    private int pageSize;

    private int currentPage;

    private Order order;

    private D searchFields;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Order {

        private String column;

        private String orderType;
    }
}