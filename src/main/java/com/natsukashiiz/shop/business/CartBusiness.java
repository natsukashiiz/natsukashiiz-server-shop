package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.CartService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class CartBusiness {

    private final CartService cartService;
    private final AuthService authService;

}
