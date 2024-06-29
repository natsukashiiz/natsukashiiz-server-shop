package com.natsukashiiz.shop.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/private")
@AllArgsConstructor
public class PrivateController {

    @GetMapping
    public String getPrivate() {
        return "Private";
    }
}
