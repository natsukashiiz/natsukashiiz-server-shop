package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.admin.model.response.QueryUserResponse;
import com.natsukashiiz.shop.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class UserController {

    private final AccountRepository accountRepository;

    @GetMapping
    public List<QueryUserResponse> getUsers() {
        return accountRepository.findAll()
                .stream()
                .map(QueryUserResponse::build)
                .collect(Collectors.toList());
    }
}
