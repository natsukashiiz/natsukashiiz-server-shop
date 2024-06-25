package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.common.SocialProviders;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.AccountSocial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountSocialRepository extends JpaRepository<AccountSocial, Long> {
    boolean existsByProviderAndEmail(SocialProviders provider, String email);

    Optional<AccountSocial> findByProviderAndEmail(SocialProviders provider, String email);

    List<AccountSocial> findByAccount(Account account);
}