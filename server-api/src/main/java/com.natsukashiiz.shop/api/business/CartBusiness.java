package com.natsukashiiz.shop.api.business;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Cart;
import com.natsukashiiz.shop.entity.ProductOption;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.CartException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.model.request.CartRequest;
import com.natsukashiiz.shop.model.response.CartItemResponse;
import com.natsukashiiz.shop.model.response.CartResponse;
import com.natsukashiiz.shop.repository.CartRepository;
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
    private final CartRepository cartRepository;

    public CartResponse queryCarts() throws BaseException {
        List<CartItemResponse> itemResponses = cartService.findAllByAccount(authService.getCurrent()).stream().map(CartItemResponse::build).collect(Collectors.toList());

        CartResponse response = new CartResponse();
        response.setItems(itemResponses);
        response.setCountSelected(itemResponses.stream().filter(CartItemResponse::getSelected).count());
        response.setTotalQuantity(itemResponses.stream().mapToInt(CartItemResponse::getQuantity).sum());
        response.setTotalSelectedQuantity(itemResponses.stream().filter(CartItemResponse::getSelected).mapToInt(CartItemResponse::getQuantity).sum());
        response.setTotalPrice(itemResponses.stream().filter(CartItemResponse::getSelected).mapToDouble(e -> e.getPrice() * e.getQuantity()).sum());

        return response;
    }

    public Integer count() throws BaseException {
        return cartService.countByAccount(authService.getCurrent());
    }


    public CartResponse updateCart(List<CartRequest> requests) throws BaseException {
        Account account = authService.getCurrent();

        for (CartRequest request : requests) {
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

            Cart cart = new Cart();
            cart.setAccount(account);
            cart.setProduct(productOption.getProduct());
            cart.setProductOption(productOption);

            cartRepository.findByAccountAndProductOption(account, productOption)
                    .ifPresent(c -> cart.setId(c.getId()));

            cart.setQuantity(request.getQuantity());
            cart.setSelected(request.getSelected());
            cartRepository.save(cart);
        }

        return queryCarts();
    }

    public CartResponse delete(Long cartId) throws BaseException {
        if (!cartService.existsById(cartId)) {
            log.warn("Upsert-[block]:(cart not found). cartId:{}", cartId);
            throw CartException.invalid();
        }
        cartService.delete(cartId);
        return queryCarts();
    }
}