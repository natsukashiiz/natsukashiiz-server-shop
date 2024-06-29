package com.natsukashiiz.shop.admin.model.response;

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
public class QueryUserResponse implements Serializable {
    private Long id;
    private String email;
    private String nickName;
    private String avatar;
    private Boolean verified;
    private Boolean deleted;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QueryUserResponse build(Account account) {
        QueryUserResponse response = new QueryUserResponse();
        response.setId(account.getId());
        response.setEmail(account.getEmail());
        response.setNickName(account.getNickName());
        response.setAvatar(account.getAvatar());
        response.setVerified(account.getVerified());
        response.setDeleted(account.getDeleted());
        response.setDeletedAt(account.getDeletedAt());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}