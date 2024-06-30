package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser(User user);

    @Modifying
    @Query("UPDATE nss_notifications SET isRead = TRUE WHERE id = :id AND user.id = :userId")
    void updateRead(@Param("id") Long id, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE nss_notifications SET isRead = TRUE WHERE user.id = :userId")
    void updateReadAll(@Param("userId") Long userId);

    boolean existsByIdAndUser(Long id, User user);
}
