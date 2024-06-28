package com.natsukashiiz.shop.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Pagination {

    private Integer page;
    private Integer limit;
}
