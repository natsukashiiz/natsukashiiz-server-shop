package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.common.SocialProvider;
import com.natsukashiiz.shop.entity.AccountSocial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountSocialRepository extends JpaRepository<AccountSocial, Long> {
    boolean existsByProviderAndEmail(SocialProvider provider, String email);

    Optional<AccountSocial> findByProviderAndEmail(SocialProvider provider, String email);
}