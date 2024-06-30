package com.natsukashiiz.shop.api.model.request;

import com.natsukashiiz.shop.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Getter
@Setter
@ToString
public class UpdateProfileRequest implements Serializable {
    private String nickName;
    private String avatar;
}