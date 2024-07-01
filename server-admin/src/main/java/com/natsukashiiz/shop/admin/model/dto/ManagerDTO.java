package com.natsukashiiz.shop.admin.model.dto;

import com.natsukashiiz.shop.common.AdminRoles;
import com.natsukashiiz.shop.entity.Admin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response for {@link com.natsukashiiz.shop.entity.Admin}
 */
@Getter
@Setter
@ToString
public class ManagerDTO implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username;
    private AdminRoles role;

    public static ManagerDTO fromEntity(Admin admin) {
        ManagerDTO response = new ManagerDTO();
        response.setId(admin.getId());
        response.setCreatedAt(admin.getCreatedAt());
        response.setUpdatedAt(admin.getUpdatedAt());
        response.setUsername(admin.getUsername());
        response.setRole(admin.getRole());
        return response;
    }

    public Admin toEntity() {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setUsername(username);
        admin.setRole(role);
        return admin;
    }
}