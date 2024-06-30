package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.common.SocialProviders;
import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.UserSocial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSocialRepository extends JpaRepository<UserSocial, Long> {
    boolean existsByProviderAndEmail(SocialProviders provider, String email);

    Optional<UserSocial> findByProviderAndEmail(SocialProviders provider, String email);

    List<UserSocial> findByUser(User user);
}