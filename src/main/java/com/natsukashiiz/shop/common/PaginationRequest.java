package com.natsukashiiz.shop.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PaginationRequest extends PageRequest {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_LIMIT = 10;

    public PaginationRequest(Integer page, Integer size) {
        super(page != null && page > 0 ? page - 1 : DEFAULT_PAGE, size != null && size > 0 ? size : DEFAULT_LIMIT, Sort.unsorted());
    }
}
