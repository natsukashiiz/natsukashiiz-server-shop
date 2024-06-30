package com.natsukashiiz.shop.admin.service;

import com.natsukashiiz.shop.common.AdminPermissions;
import com.natsukashiiz.shop.common.AdminRoles;
import com.natsukashiiz.shop.common.Roles;
import com.natsukashiiz.shop.entity.Admin;
import com.natsukashiiz.shop.exception.AuthException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.LoginException;
import com.natsukashiiz.shop.exception.SignUpException;
import com.natsukashiiz.shop.admin.model.request.LoginRequest;
import com.natsukashiiz.shop.admin.model.request.SignUpRequest;
import com.natsukashiiz.shop.model.request.RefreshTokenRequest;
import com.natsukashiiz.shop.model.resposne.TokenResponse;
import com.natsukashiiz.shop.repository.AdminRepository;
import com.natsukashiiz.shop.service.TokenService;
import com.natsukashiiz.shop.utils.RedisKeyUtils;
import com.natsukashiiz.shop.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Log4j2
@AllArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private RedisTemplate<String, String> redisTemplate;

    public TokenResponse login(LoginRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        if (ValidationUtils.invalidUsername(request.getUsername())) {
            log.warn("Login-[block]:(invalid username). request:{}", request);
            throw LoginException.invalidUsername();
        }

        Optional<Admin> adminOptional = adminRepository.findByUsername(request.getUsername());
        if (!adminOptional.isPresent()) {
            log.warn("Login-[block]:(not found account). request:{}", request);
            throw LoginException.invalid();
        }

        Admin admin = adminOptional.get();
        if (passwordNotMatch(request.getPassword(), admin.getPassword())) {
            log.warn("Login-[block]:(password not matches). request:{}", request);
            throw LoginException.invalid();
        }

        return createTokenResponse(admin, httpServletRequest);
    }

    public TokenResponse signUp(SignUpRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        if (ValidationUtils.invalidUsername(request.getUsername())) {
            log.warn("SignUp-[block]:(invalid username). request:{}", request);
            throw LoginException.invalidUsername();
        }

        if (adminRepository.existsByUsername(request.getUsername())) {
            log.warn("SignUp-[block]:(exists username). request:{}", request);
            throw SignUpException.usernameExist();
        }

        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setRole(request.getRole());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        adminRepository.save(admin);

        return createTokenResponse(admin, httpServletRequest);
    }

    public TokenResponse refresh(RefreshTokenRequest request, HttpServletRequest httpServletRequest) throws BaseException {

        if (ObjectUtils.isEmpty(request.getRefreshToken())) {
            log.warn("Refresh-[block]:(refresh token is empty)");
            throw AuthException.unauthorized();
        }

        Jwt jwt = tokenService.decode(request.getRefreshToken());

        if (jwt == null) {
            log.warn("Refresh-[block]:(jwt is null)");
            throw AuthException.unauthorized();
        }

        Long adminId = Long.parseLong(jwt.getSubject());

        if (!tokenService.isRefreshToken(jwt)) {
            log.warn("Refresh-[block]:(not refresh token)");
            throw AuthException.unauthorized();
        }

        if (ObjectUtils.isEmpty(adminId)) {
            log.warn("Refresh-[block]:(adminId is empty). adminId:{}", adminId);
            throw AuthException.unauthorized();
        }

        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (!adminOptional.isPresent()) {
            log.warn("Refresh-[block]:(not found account). adminId:{}", adminId);
            throw AuthException.unauthorized();
        }

        Admin admin = adminOptional.get();

        String redisRefreshTokenKey = RedisKeyUtils.authRefreshTokenKey(admin.getId());
        String redisRefreshTokenValue = redisTemplate.opsForValue().get(redisRefreshTokenKey);
        if (ObjectUtils.isEmpty(redisRefreshTokenValue)) {
            log.warn("Refresh-[block]:(not found redis refresh token). adminId:{}", admin.getId());
            throw AuthException.unauthorized();
        }

        if (!jwt.getId().equals(redisRefreshTokenValue)) {
            log.warn("Refresh-[block]:(refresh token not match). adminId:{}", admin.getId());
            throw AuthException.unauthorized();
        }

        return createTokenResponse(admin, httpServletRequest);
    }

    public Admin getAdmin() throws BaseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("GetAdmin-[block]:(authentication is null)");
            throw AuthException.unauthorized();
        }

        if (!authentication.isAuthenticated()) {
            log.warn("GetAdmin-[block]:(not authenticated)");
            throw AuthException.unauthorized();
        }

        Jwt jwt = (Jwt) authentication.getCredentials();

        if (jwt == null) {
            log.warn("GetAdmin-[block]:(jwt is null)");
            throw AuthException.unauthorized();
        }

        if (!tokenService.isAccessToken(jwt)) {
            log.warn("GetAdmin-[block]:(not access token)");
            throw AuthException.unauthorized();
        }

        String adminId = authentication.getName();

        if (ObjectUtils.isEmpty(adminId)) {
            log.warn("GetAdmin-[block]:(adminId is empty). adminId:{}", adminId);
            throw AuthException.unauthorized();
        }

        Optional<Admin> adminOptional = adminRepository.findById(Long.parseLong(adminId));
        if (!adminOptional.isPresent()) {
            log.warn("GetAdmin-[block]:(not found account). adminId:{}", adminId);
            throw AuthException.unauthorized();
        }

        Admin admin = adminOptional.get();

        String redisAccessTokenKey = RedisKeyUtils.authAccessTokenKey(admin.getId());
        String redisAccessTokenValue = redisTemplate.opsForValue().get(redisAccessTokenKey);
        if (ObjectUtils.isEmpty(redisAccessTokenValue)) {
            log.warn("GetAdmin-[block]:(not found redis access token). adminId:{}", admin.getId());
            throw AuthException.unauthorized();
        }

        if (!jwt.getId().equals(redisAccessTokenValue)) {
            log.warn("GetAdmin-[block]:(access token not match). adminId:{}", admin.getId());
            throw AuthException.unauthorized();
        }

        return admin;
    }

    public boolean anonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ObjectUtils.isEmpty(authentication) || authentication.getPrincipal().equals("anonymousUser");
    }

    public boolean passwordNotMatch(String raw, String hash) {
        return !passwordEncoder.matches(raw, hash);
    }

    public TokenResponse createTokenResponse(Admin admin, HttpServletRequest httpServletRequest) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", admin.getRole());

        Set<String> permissions;
        if (admin.getRole().equals(AdminRoles.SUPER_ADMIN)) {
            permissions = new HashSet<>();
            for (AdminPermissions permission : AdminPermissions.values()) {
                permissions.add(permission.getPermission());
            }
        } else {
            permissions = Collections.singleton(AdminPermissions.ADMIN_READ.getPermission());
        }
        claims.put("authorities", permissions);

        String accessToken = tokenService.generateAccessToken(admin.getId(), claims);
        String redisAccessTokenKey = RedisKeyUtils.authAccessTokenKey(admin.getId());
        redisTemplate.opsForValue().set(redisAccessTokenKey, tokenService.decode(accessToken).getId());

        String refreshToken = tokenService.generateRefreshToken(admin.getId());
        String redisRefreshTokenKey = RedisKeyUtils.authRefreshTokenKey(admin.getId());
        redisTemplate.opsForValue().set(redisRefreshTokenKey, tokenService.decode(refreshToken).getId());

        return TokenResponse.build(accessToken, refreshToken);
    }
}
