package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAccount(Account account);
}
