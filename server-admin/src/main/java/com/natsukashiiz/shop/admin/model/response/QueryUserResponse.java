package com.natsukashiiz.shop.admin.model.response;

import com.natsukashiiz.shop.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link User}
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

    public static QueryUserResponse build(User user) {
        QueryUserResponse response = new QueryUserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setNickName(user.getNickName());
        response.setAvatar(user.getAvatar());
        response.setVerified(user.getVerified());
        response.setDeleted(user.getDeleted());
        response.setDeletedAt(user.getDeletedAt());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}