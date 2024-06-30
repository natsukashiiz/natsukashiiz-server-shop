package com.natsukashiiz.shop.api.model.response;

import com.natsukashiiz.shop.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link User}
 */
@Getter
@Setter
@ToString
public class ProfileResponse implements Serializable {
    private Long id;
    private String email;
    private String nickName;
    private String avatar;
    private LocalDateTime createdAt;
    private List<UserSocialResponse> socials;

    public static ProfileResponse build(User user) {
        ProfileResponse response = new ProfileResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setNickName(user.getNickName());
        response.setAvatar(user.getAvatar());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}