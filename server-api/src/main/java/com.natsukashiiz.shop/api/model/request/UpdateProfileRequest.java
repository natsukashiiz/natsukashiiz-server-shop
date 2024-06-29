package com.natsukashiiz.shop.api.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.Account}
 */
@Getter
@Setter
@ToString
public class UpdateProfileRequest implements Serializable {
    private String nickName;
    private String avatar;
}