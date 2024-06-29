package com.natsukashiiz.shop.model.resposne;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileStoreResponse {
    private String name;
    private String url;
    private String type;
    private long size;
}
