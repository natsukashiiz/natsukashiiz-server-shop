package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {
    @Modifying
    @Query("UPDATE nss_point SET point = point + :point WHERE user.id = :userId")
    void increasePoint(@Param("userId") long userId, @Param("point") double point);

    Optional<Point> findByUser(User user);
}
