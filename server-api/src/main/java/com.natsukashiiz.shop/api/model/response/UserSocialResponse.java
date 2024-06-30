package com.natsukashiiz.shop.api.model.response;

import com.natsukashiiz.shop.common.SocialProviders;
import com.natsukashiiz.shop.entity.UserSocial;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.UserSocial}
 */
@Getter
@Setter
@ToString
public class UserSocialResponse implements Serializable {
    private Long id;
    private SocialProviders provider;
    private String email;
    private LocalDateTime createdAt;

    public static UserSocialResponse build(UserSocial userSocial) {
        UserSocialResponse response = new UserSocialResponse();
        response.setId(userSocial.getId());
        response.setProvider(userSocial.getProvider());
        response.setEmail(userSocial.getEmail());
        response.setCreatedAt(userSocial.getCreatedAt());
        return response;
    }

    public static List<UserSocialResponse> buildList(List<UserSocial> userSocials) {
        return userSocials.stream()
                .map(UserSocialResponse::build)
                .collect(Collectors.toList());
    }
}