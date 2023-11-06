package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.ApiResponse;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Order;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.entity.Wallet;
import com.natsukashiiz.shop.model.request.BuyRequest;
import com.natsukashiiz.shop.model.response.BuyResponse;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.repository.*;
import com.natsukashiiz.shop.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
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

    @Transactional
    public ApiResponse<BuyResponse> buy(List<BuyRequest> requests, Authentication authentication) {
        Account account = accountRepository.findByEmail(authentication.getName()).get();

        Optional<Wallet> walletOptional = walletRepository.findByAccount(account);
        if (!walletOptional.isPresent()) {
            log.warn("Buy-[block]:(not found wallet). accountId:{}", account.getId());
            return ResponseUtils.fail(-1, "You don't have wallet");
        }

        double paid = 0;
        int quantity = 0;
        List<OrderResponse> orders = new LinkedList<>();
        for (BuyRequest req : requests) {
            Optional<Product> productOptional = productRepository.findById(req.getProductId());
            if (!productOptional.isPresent()) {
                log.warn("Buy-[block]:(not found product). req:{}", req);
                return ResponseUtils.fail(-1, String.format("Product %d not found", req.getProductId()));

            }

            Product product = productOptional.get();
            if (product.getQuantity() - req.getQuantity() < 0) {
                log.warn("Buy-[block]:(Insufficient quantity of products). req:{}, product:{}", req, product);
                return ResponseUtils.fail(-1, "Insufficient quantity of products");
            }

            productRepository.decreaseQuantity(product.getId(), req.getQuantity());
            pointRepository.increasePoint(account.getId(), 10.25 * req.getQuantity());

            double totalPrice = product.getPrice() * req.getQuantity();

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
                    .time(LocalDateTime.now())
                    .build();
            orders.add(response);
            paid += totalPrice;
            quantity += req.getQuantity();
        }

        double balance = walletOptional.get().getBalance();
        if (balance - paid < 0) {
            log.warn("Buy-[block]:(Insufficient balance). requests:{}, balance:{}, paid:{}", requests, balance, paid);
            return ResponseUtils.fail(-1, "Insufficient balance");
        }

        walletRepository.decreaseBalance(account.getId(), paid);

        return ResponseUtils.success(
                BuyResponse.builder()
                        .orders(orders)
                        .quantity(quantity)
                        .paid(paid)
                        .build()
        );
    }
}
