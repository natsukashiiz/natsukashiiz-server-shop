package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Order;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.entity.Wallet;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.OrderException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.exception.WalletException;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;
    private final ProductRepository productRepository;
    private final PointRepository pointRepository;
    private final CartRepository cartRepository;

    public OrderResponse myOrderById(UUID orderId) throws BaseException {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw OrderException.invalid();
        }
        return buildResponse(orderOptional.get());
    }

    public List<OrderResponse> myOrders(Account account) {
        return orderRepository.findAllByAccount(account)
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> myOrderListByLatest(Account account) {
        return orderRepository.findAllByAccountOrderByCreatedAtDesc(account)
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }


    @Transactional(rollbackOn = BaseException.class)
    public List<OrderResponse> create(List<CreateOrderRequest> requests, Account account) throws BaseException {
        Optional<Wallet> walletOptional = walletRepository.findByAccount(account);
        if (!walletOptional.isPresent()) {
            log.warn("Buy-[block]:(not found wallet). account:{}", account);
            throw WalletException.invalid();
        }

        Map<Long, Product> productMap = productRepository.findAllById(requests.stream().map(CreateOrderRequest::getProductId).collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

//        double paid = requests.stream()
//                .mapToDouble(e -> productMap.get(e.getProductId()).getPrice() * e.getQuantity())
//                .sum();
//
//        double balance = walletOptional.get().getBalance();
//        if (balance - paid < 0) {
//            log.warn("Buy-[block]:(Insufficient balance). requests:{}, balance:{}, paid:{}", requests, balance, paid);
//            throw WalletException.insufficient();
//        }
//        walletRepository.decreaseBalance(account.getId(), paid);

        List<OrderResponse> responses = new LinkedList<>();
        for (CreateOrderRequest req : requests) {
            Product product = productMap.get(req.getProductId());
            if (Objects.isNull(product)) {
                log.warn("Buy-[block]:(not found product). req:{}", req);
                throw ProductException.invalid();
            }

            if (product.getQuantity() - req.getQuantity() < 0) {
                log.warn("Buy-[block]:(Insufficient quantity of products). req:{}, product:{}", req, product);
                throw ProductException.insufficient();
            }

            productRepository.decreaseQuantity(product.getId(), req.getQuantity());
//            pointRepository.increasePoint(account.getId(), 10.25 * req.getQuantity());
            cartRepository.deleteByProductAndAccount(product, account);

            double totalPrice = product.getPrice() * req.getQuantity();

            Order order = new Order();
            order.setAccount(account);
            order.setProductName(product.getName());
            order.setPrice(product.getPrice());
            order.setQuantity(req.getQuantity());
            order.setTotalPrice(totalPrice);
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);

            responses.add(buildResponse(order));
        }

        return responses;
    }

    @Transactional
    public void updateChargeId(UUID orderId, String chargeId) {
        orderRepository.updateChargeId(orderId, chargeId);
    }

    @Transactional
    public void updateStatus(UUID orderId, OrderStatus status) {
        orderRepository.updateStatus(orderId, status);
    }

    private OrderResponse buildResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .productName(order.getProductName())
                .productPrice(order.getPrice())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .time(order.getCreatedAt())
                .build();
    }
}
