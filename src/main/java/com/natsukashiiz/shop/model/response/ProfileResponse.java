package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Account;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.Account}
 */
@Getter
@Setter
@ToString
public class ProfileResponse implements Serializable {
    private Long id;
    private String email;
    private LocalDateTime createdAt;

    public static ProfileResponse build(Account account) {
        ProfileResponse response = new ProfileResponse();
        response.setId(account.getId());
        response.setEmail(account.getEmail());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }
}