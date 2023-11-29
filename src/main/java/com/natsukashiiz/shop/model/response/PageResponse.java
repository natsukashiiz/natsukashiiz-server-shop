package com.natsukashiiz.shop.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> {

    private T list;
    private long total;
}
