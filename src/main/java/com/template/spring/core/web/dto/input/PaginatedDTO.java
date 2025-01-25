package com.template.spring.core.web.dto.input;

import lombok.*;


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