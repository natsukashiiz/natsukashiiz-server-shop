package com.natsukashiiz.shop.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> implements Serializable {

    private T list;
    private long total;
}
