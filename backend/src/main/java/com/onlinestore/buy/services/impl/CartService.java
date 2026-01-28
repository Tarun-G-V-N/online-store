package com.onlinestore.buy.services.impl;

import com.onlinestore.buy.dtos.CartDto;
import com.onlinestore.buy.entities.Cart;
import com.onlinestore.buy.entities.User;
import com.onlinestore.buy.repositories.CartItemRepository;
import com.onlinestore.buy.repositories.CartRepository;
import com.onlinestore.buy.services.ICartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public Cart initializeNewCartForUser(User user) {
        try {
            return getCartByUser(user.getId());
        } catch (Exception e) {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        }
    }

    @Override
    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    @Override
    public Cart getCartByUser(Long userId) {
        return Optional.ofNullable(cartRepository.findByUserId(userId)).orElseThrow(() -> new EntityNotFoundException("Cart not found. Please add any product to the cart."));
    }

    @Override
    @Transactional
    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cartRepository.deleteById(cartId);
    }

    @Override
    public double getTotalAmount(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cart not found")).getTotalAmount();
    }

    @Override
    public CartDto convertToDto(Cart cart) {
        return modelMapper.map(cart, CartDto.class);
    }
}
