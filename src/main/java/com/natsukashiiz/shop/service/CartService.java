package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Cart;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.repository.CartRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class CartService {

    private final CartRepository cartRepository;

    public Optional<Cart> findByProductAndAccount(Product product, Account account) {
        return cartRepository.findByProductAndAccount(product, account);
    }

    public List<Cart> findAllByAccount(Account account) {
        return cartRepository.findByAccount(account);
    }

    public Cart create(Product product, Integer quantity, Account account) {
        Cart cart = new Cart();
        cart.setAccount(account);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    public Cart update(Cart cart) {
        return cartRepository.save(cart);
    }

    public void delete(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public boolean existsById(Long cartId) {
        return cartRepository.existsById(cartId);
    }

    public boolean existsByProductAndAccount(Product product, Account account) {
        return cartRepository.existsByProductAndAccount(product, account);
    }
}
