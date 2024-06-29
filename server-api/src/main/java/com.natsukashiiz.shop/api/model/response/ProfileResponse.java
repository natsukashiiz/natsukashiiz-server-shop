package com.natsukashiiz.shop.api.model.response;

import com.natsukashiiz.shop.entity.Account;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.Account}
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
    private List<AccountSocialResponse> socials;

    public static ProfileResponse build(Account account) {
        ProfileResponse response = new ProfileResponse();
        response.setId(account.getId());
        response.setEmail(account.getEmail());
        response.setNickName(account.getNickName());
        response.setAvatar(account.getAvatar());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }
}