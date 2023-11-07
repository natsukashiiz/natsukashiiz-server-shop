package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Order;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.entity.Wallet;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.exception.WalletException;
import com.natsukashiiz.shop.model.request.BuyRequest;
import com.natsukashiiz.shop.model.response.BuyResponse;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.repository.OrderRepository;
import com.natsukashiiz.shop.repository.PointRepository;
import com.natsukashiiz.shop.repository.ProductRepository;
import com.natsukashiiz.shop.repository.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ProductService {
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;
    private final PointRepository pointRepository;
    private final ProductRepository productRepository;

    @Cacheable(value = "products")
    public List<ProductResponse> getList() {
        return productRepository.findAll()
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(rollbackOn = BaseException.class)
    public BuyResponse buy(List<BuyRequest> requests, Account account) throws BaseException {
        Optional<Wallet> walletOptional = walletRepository.findByAccount(account);
        if (!walletOptional.isPresent()) {
            log.warn("Buy-[block]:(not found wallet). account:{}", account);
            throw WalletException.invalid();
        }

        double paid = 0;
        int quantity = 0;
        List<OrderResponse> orders = new LinkedList<>();
        for (BuyRequest req : requests) {
            Optional<Product> productOptional = productRepository.findById(req.getProductId());
            if (!productOptional.isPresent()) {
                log.warn("Buy-[block]:(not found product). req:{}", req);
                throw ProductException.invalid();
            }

            Product product = productOptional.get();
            if (product.getQuantity() - req.getQuantity() < 0) {
                log.warn("Buy-[block]:(Insufficient quantity of products). req:{}, product:{}", req, product);
                throw ProductException.insufficient();
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
            throw WalletException.insufficient();
        }

        walletRepository.decreaseBalance(account.getId(), paid);

        return BuyResponse.builder()
                .orders(orders)
                .quantity(quantity)
                .paid(paid)
                .build();
    }
}
