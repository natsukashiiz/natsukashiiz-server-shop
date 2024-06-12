package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.entity.Cart;
import com.natsukashiiz.shop.entity.ProductOption;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.CartException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.model.request.CartRequest;
import com.natsukashiiz.shop.model.response.CartResponse;
import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.CartService;
import com.natsukashiiz.shop.service.ProductOptionService;
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
    private final ProductOptionService optionService;

    public List<CartResponse> getAll() throws BaseException {
        return cartService.findAllByAccount(authService.getCurrent())
                .stream()
                .map(e -> {
                    ProductResponse product = ProductResponse.build(e.getProduct());
                    return CartResponse.build(e, product);
                })
                .collect(Collectors.toList());
    }

    public Integer count() throws BaseException {
        return cartService.countByAccount(authService.getCurrent());
    }

    public CartResponse create(ProductOption productOption, Integer quantity) throws BaseException {
        return CartResponse.build(cartService.create(productOption, quantity, authService.getCurrent()));
    }

    public CartResponse update(ProductOption productOption, Integer quantity) throws BaseException {
        Optional<Cart> cartOptional = cartService.findByProductOptionAndAccount(productOption, authService.getCurrent());
        if (!cartOptional.isPresent()) {
            log.warn("Update-[block]:(product option in cart not found). productOption:{}", productOption);
            throw CartException.invalid();
        }
        Cart cart = cartOptional.get();

        if (Objects.equals(cart.getQuantity(), quantity)) {
            // ถ้าจำนวนเท่าเดิมให้ตอบกลับเลย
            return CartResponse.build(cart);
        }

        cart.setQuantity(quantity);
        return CartResponse.build(cartService.update(cart));
    }

    // กระบวนการในการ inserts rows เข้าไปใน db table ถ้าข้อมูลนั้นยังไม่มี หรือ update ข้อมูลถ้าข้อมูลนั้นมีแล้ว
    public CartResponse upsert(CartRequest request) throws BaseException {
        Optional<ProductOption> optionOptional = optionService.getById(request.getOptionId());
        if (!optionOptional.isPresent()) {
            log.warn("Upsert-[block]:(product option not found). request:{}", request);
            throw ProductException.invalid();
        }
        ProductOption productOption = optionOptional.get();

        if (!Objects.equals(productOption.getProduct().getId(), request.getProductId())) {
            log.warn("Upsert-[block]:(invalid product). request:{}", request);
            throw ProductException.invalid();
        }

        if ((productOption.getQuantity() - request.getQuantity()) < 0) {
            log.warn("Upsert-[block]:(product option insufficient). request:{}", request);
            throw ProductException.insufficient();
        }

        Optional<Cart> cartOptional = cartService.findByProductOptionAndAccount(productOption, authService.getCurrent());
        if (cartOptional.isPresent()) {
            if (request.getQuantity() == 1) {
                return update(productOption, cartOptional.get().getQuantity() + 1);
            } else {
                return update(productOption, request.getQuantity());
            }
        } else {
            return create(productOption, request.getQuantity());
        }
    }

    public void delete(Long cartId) throws BaseException {
        if (!cartService.existsById(cartId)) {
            log.warn("Upsert-[block]:(cart not found). cartId:{}", cartId);
            throw CartException.invalid();
        }

        cartService.delete(cartId);
    }
}