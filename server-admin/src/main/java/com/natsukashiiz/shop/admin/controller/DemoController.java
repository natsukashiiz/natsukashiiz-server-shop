package com.natsukashiiz.shop.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/demo")
@AllArgsConstructor
public class DemoController {

    @GetMapping
    public String demo() {
        return "Hello, Admin!";
    }
}
