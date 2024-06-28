package com.natsukashiiz.shop.service;

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
import com.natsukashiiz.shop.repository.ProductOptionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class CartService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final ProductOptionRepository productOptionRepository;

    public CartResponse queryAllCart() throws BaseException {
        Account account = authService.getAccount();

        List<Cart> carts = cartRepository.findByAccount(account);
        List<CartItemResponse> items = CartItemResponse.buildList(carts);

        CartResponse response = new CartResponse();
        response.setItems(items);
        response.setCountSelected(items.stream().filter(CartItemResponse::getSelected).count());
        response.setTotalQuantity(items.stream().mapToInt(CartItemResponse::getQuantity).sum());
        response.setTotalSelectedQuantity(items.stream().filter(CartItemResponse::getSelected).mapToInt(CartItemResponse::getQuantity).sum());
        response.setTotalPrice(items.stream().filter(CartItemResponse::getSelected).mapToDouble(e -> e.getPrice() * e.getQuantity()).sum());

        return response;
    }

    public CartResponse updateCart(List<CartRequest> requests) throws BaseException {
        Account account = authService.getAccount();

        for (CartRequest request : requests) {
            Optional<ProductOption> optionOptional = productOptionRepository.findById(request.getOptionId());
            if (!optionOptional.isPresent()) {
                log.warn("UpdateCart-[block]:(product option not found). request:{}, accountId:{}", request, account.getId());
                throw ProductException.invalid();
            }
            ProductOption productOption = optionOptional.get();

            if (!Objects.equals(productOption.getProduct().getId(), request.getProductId())) {
                log.warn("UpdateCart-[block]:(invalid product). request:{}, accountId:{}", request, account.getId());
                throw ProductException.invalid();
            }

            if ((productOption.getQuantity() - request.getQuantity()) < 0) {
                log.warn("UpdateCart-[block]:(product option insufficient). request:{}, accountId:{}", request, account.getId());
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

        return queryAllCart();
    }

    public CartResponse delete(Long cartId) throws BaseException {
        Account account = authService.getAccount();

        if (!cartRepository.existsByIdAndAccount(cartId, account)) {
            log.warn("Delete-[block]:(cart not found). cartId:{}, accountId:{}", cartId, account.getId());
            throw CartException.invalid();
        }

        cartRepository.deleteById(cartId);

        return queryAllCart();
    }

    public Long countCart() throws BaseException {
        Account account = authService.getAccount();
        return cartRepository.countByAccount(account);
    }
}
