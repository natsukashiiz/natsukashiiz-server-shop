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

    private final CartService cartService;
    private final AuthService authService;
    private final ProductService productService;

    public List<CartResponse> getAll() throws BaseException {
        return cartService.findAll(authService.getCurrent())
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
            throw CartException.invalid();
        }
        Cart cart = cartOptional.get();

        if (Objects.equals(cart.getQuantity(), quantity)) {
            return buildResponse(cart);
        }

        cart.setQuantity(quantity);
        return buildResponse(cartService.update(cart, authService.getCurrent()));
    }

    public void delete(Long cartId) throws BaseException {
        if (!cartService.existsById(cartId)) {
            throw CartException.invalid();
        }

        cartService.delete(cartId, authService.getCurrent());
    }

    public CartResponse upsert(CartRequest request) throws BaseException {
        Optional<Product> productOptional = productService.getById(request.getProductId());
        if (!productOptional.isPresent()) {
            throw ProductException.invalid();
        }
        Product product = productOptional.get();

        if (request.getQuantity() < 1) {
            throw ProductException.invalidQuantity();
        }

        if (product.getQuantity() - request.getQuantity() < 0) {
            throw ProductException.insufficient();
        }

        if (cartService.existsByProductAndAccount(product, authService.getCurrent())) {
            return update(product, request.getQuantity());
        } else {
            return create(product, request.getQuantity());
        }
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
