package com.natsukashiiz.shop.admin.service;

import com.natsukashiiz.shop.admin.model.dto.UserDTO;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Admin;
import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.UserException;
import com.natsukashiiz.shop.model.resposne.PageResponse;
import com.natsukashiiz.shop.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public PageResponse<List<UserDTO>> queryAllUsers(UserDTO request, PaginationRequest pagination) {
        Example<User> example = Example.of(request.toEntity(), ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase());
        Page<User> page = userRepository.findAll(example, pagination);
        List<UserDTO> users = page.getContent().stream().map(UserDTO::fromEntity).collect(Collectors.toList());
        return new PageResponse<>(users, page.getTotalElements());
    }

    public UserDTO queryUserById(Long userId) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            log.warn("QueryUserById-[block]:(user not found). userId:{}, adminId:{}", userId, admin.getId());
            throw UserException.invalid();
        }

        return UserDTO.fromEntity(userOptional.get());
    }

    public UserDTO createUser(UserDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        if (ObjectUtils.isEmpty(request.getEmail())) {
            log.warn("CreateUser-[block]:(email is empty). adminId:{}", admin.getId());
            throw UserException.invalidEmail();
        }

        if (ObjectUtils.isEmpty(request.getNickName())) {
            log.warn("CreateUser-[block]:(nickName is empty). adminId:{}", admin.getId());
            throw UserException.invalidNickName();
        }

        if (ObjectUtils.isEmpty(request.getVerified())) {
            log.warn("CreateUser-[block]:(verified is empty). adminId:{}", admin.getId());
            throw UserException.invalidVerified();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("CreateUser-[block]:(email exist). email:{}, adminId:{}", request.getEmail(), admin.getId());
            throw UserException.emailExist();
        }

        if (userRepository.existsByNickName(request.getNickName())) {
            log.warn("CreateUser-[block]:(nickName exist). nickName:{}, adminId:{}", request.getNickName(), admin.getId());
            throw UserException.nickNameExist();
        }

        User user = request.toEntity();
        User saved = userRepository.save(user);
        return UserDTO.fromEntity(saved);
    }

    public UserDTO updateUserById(Long userId, UserDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            log.warn("UpdateUserById-[block]:(user not found). userId:{}, adminId:{}", userId, admin.getId());
            throw UserException.invalid();
        }

        User user = userOptional.get();
        user.setVerified(request.getVerified());

        User saved = userRepository.save(user);
        return UserDTO.fromEntity(saved);
    }

    public void deleteUserById(Long userId) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            log.warn("DeleteUserById-[block]:(user not found). userId:{}, adminId:{}", userId, admin.getId());
            throw UserException.invalid();
        }

        User user = userOptional.get();
        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void restoreUserById(Long userId) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            log.warn("RestoreUserById-[block]:(user not found). userId:{}, adminId:{}", userId, admin.getId());
            throw UserException.invalid();
        }

        User user = userOptional.get();
        user.setDeleted(false);
        user.setDeletedAt(null);
        userRepository.save(user);
    }
}
