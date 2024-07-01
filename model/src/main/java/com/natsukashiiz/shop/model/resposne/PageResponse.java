package com.natsukashiiz.shop.model.resposne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> implements Serializable {

    private T list;
    private long total;
}
