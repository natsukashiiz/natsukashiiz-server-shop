package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Order;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.entity.Wallet;
import com.natsukashiiz.shop.model.request.BuyRequest;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;
    private final PointRepository pointRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    public ResponseEntity<OrderResponse> buy(BuyRequest req, Authentication authentication) {
        Account account = accountRepository.findByEmail(authentication.getName()).get();

        Optional<Product> productOptional = productRepository.findById(req.getProductId());
        if (!productOptional.isPresent()) {
            log.warn("Buy-[block]:(not found product). req:{}", req);
            return ResponseEntity.badRequest().build();
        }

        Product product = productOptional.get();
        if (product.getQuantity() - req.getQuantity() < 0) {
            log.warn("Buy-[block]:(Insufficient quantity of products). req:{}", req);
            return ResponseEntity.badRequest().build();
        }

        Optional<Wallet> walletOptional = walletRepository.findByAccount(account);
        if (!walletOptional.isPresent()) {
            log.warn("Buy-[block]:(not found wallet). req:{}", req);
            return ResponseEntity.badRequest().build();
        }
        Wallet wallet = walletOptional.get();

        double totalPrice = product.getPrice() * req.getQuantity();
        if (totalPrice > wallet.getBalance()) {
            log.warn("Buy-[block]:(Insufficient balance). req:{}", req);
            return ResponseEntity.badRequest().build();
        }

        walletRepository.decreaseBalance(account.getId(), totalPrice);
        productRepository.reduceQuantity(product.getId(), req.getQuantity());
        pointRepository.increasePoint(account.getId(), 10.50);

        Order order = new Order();
        order.setAccount(account);
        order.setProductName(product.getName());
        order.setPrice(product.getPrice());
        order.setQuantity(req.getQuantity());
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        OrderResponse response = OrderResponse.builder()
                .orderId(order.getId())
                .productName(order.getProductName())
                .productPrice(order.getPrice())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .time(order.getCreatedAt())
                .build();
        return ResponseEntity.ok(response);
    }
}
