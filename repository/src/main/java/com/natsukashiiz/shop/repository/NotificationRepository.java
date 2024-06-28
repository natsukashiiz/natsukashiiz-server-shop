package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByAccount(Account account);

    @Modifying
    @Query("UPDATE sp_notifications SET isRead = TRUE WHERE id = :id AND account.id = :accountId")
    void updateRead(@Param("id") Long id, @Param("accountId") Long accountId);

    @Modifying
    @Query("UPDATE sp_notifications SET isRead = TRUE WHERE account.id = :accountId")
    void updateReadAll(@Param("accountId") Long accountId);

    boolean existsByIdAndAccount(Long id, Account account);
}
