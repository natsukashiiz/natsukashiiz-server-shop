package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.entity.Cart;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.CartException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.model.request.CartRequest;
import com.natsukashiiz.shop.model.response.CartResponse;
import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.CartService;
import com.natsukashiiz.shop.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class CartBusiness {

    private final AuthService authService;
    private final CartService cartService;
    private final ProductService productService;

    public List<CartResponse> getAll() throws BaseException {
        return cartService.findAllByAccount(authService.getCurrent())
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    public CartResponse create(Product product, Integer quantity) throws BaseException {
        return buildResponse(cartService.create(product, quantity, authService.getCurrent()));
    }

    public CartResponse update(Product product, Integer quantity) throws BaseException {
        Optional<Cart> cartOptional = cartService.findByProductAndAccount(product, authService.getCurrent());
        if (!cartOptional.isPresent()) {
            log.warn("Update-[block]:(product in cart not found). product:{}", product);
            throw CartException.invalid();
        }
        Cart cart = cartOptional.get();

        if (Objects.equals(cart.getQuantity(), quantity)) {
            // ถ้าจำนวนเท่าเดิมให้ตอบกลับเลย
            return buildResponse(cart);
        }

        cart.setQuantity(quantity);
        return buildResponse(cartService.update(cart));
    }

    // กระบวนการในการ inserts rows เข้าไปใน db table ถ้าข้อมูลนั้นยังไม่มี หรือ update ข้อมูลถ้าข้อมูลนั้นมีแล้ว
    public CartResponse upsert(CartRequest request) throws BaseException {
        Optional<Product> productOptional = productService.getById(request.getProductId());
        if (!productOptional.isPresent()) {
            log.warn("Upsert-[block]:(product not found). request:{}", request);
            throw ProductException.invalid();
        }
        Product product = productOptional.get();

        if (request.getQuantity() < 1) {
            log.warn("Upsert-[block]:(invalid quantity). request:{}", request);
            throw ProductException.invalidQuantity();
        }

        if (product.getQuantity() - request.getQuantity() < 0) {
            log.warn("Upsert-[block]:(product insufficient). request:{}", request);
            throw ProductException.insufficient();
        }

        if (cartService.existsByProductAndAccount(product, authService.getCurrent())) {
            return update(product, request.getQuantity());
        } else {
            return create(product, request.getQuantity());
        }
    }

    public void delete(Long cartId) throws BaseException {
        if (!cartService.existsById(cartId)) {
            log.warn("Upsert-[block]:(cart not found). cartId:{}", cartId);
            throw CartException.invalid();
        }

        cartService.delete(cartId);
    }

    private CartResponse buildResponse(Cart cart) {
        return CartResponse.builder()
                .product(ProductResponse.builder()
                        .id(cart.getProduct().getId())
                        .name(cart.getProduct().getName())
                        .price(cart.getProduct().getPrice())
                        .quantity(cart.getProduct().getQuantity())
                        .build())
                .quantity(cart.getQuantity())
                .build();
    }
}
