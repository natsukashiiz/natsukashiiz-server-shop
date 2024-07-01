package com.natsukashiiz.shop.admin.model.dto;

import com.natsukashiiz.shop.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response for {@link com.natsukashiiz.shop.entity.User}
 */
@Getter
@Setter
@ToString
public class UserDTO implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String email;
    private String nickName;
    private String avatar;
    private Boolean verified;
    private Boolean deleted;
    private LocalDateTime deletedAt;

    public static UserDTO fromEntity(User user) {
        UserDTO response = new UserDTO();
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

    public User toEntity() {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setNickName(nickName);
        user.setAvatar(avatar);
        user.setVerified(verified);
        user.setDeleted(deleted);
        user.setDeletedAt(deletedAt);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);
        return user;
    }
}