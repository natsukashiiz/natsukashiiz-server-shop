package com.natsukashiiz.shop.api.model.response;

import com.natsukashiiz.shop.common.SocialProviders;
import com.natsukashiiz.shop.entity.AccountSocial;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.AccountSocial}
 */
@Getter
@Setter
@ToString
public class AccountSocialResponse implements Serializable {
    private Long id;
    private SocialProviders provider;
    private String email;
    private LocalDateTime createdAt;

    public static AccountSocialResponse build(AccountSocial accountSocial) {
        AccountSocialResponse response = new AccountSocialResponse();
        response.setId(accountSocial.getId());
        response.setProvider(accountSocial.getProvider());
        response.setEmail(accountSocial.getEmail());
        response.setCreatedAt(accountSocial.getCreatedAt());
        return response;
    }

    public static List<AccountSocialResponse> buildList(List<AccountSocial> accountSocials) {
        return accountSocials.stream()
                .map(AccountSocialResponse::build)
                .collect(Collectors.toList());
    }
}