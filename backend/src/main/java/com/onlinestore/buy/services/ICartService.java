package com.onlinestore.buy.services;

import com.onlinestore.buy.dtos.CartDto;
import com.onlinestore.buy.entities.Cart;
import com.onlinestore.buy.entities.User;

public interface ICartService {
    Cart initializeNewCartForUser(User user);
    Cart getCart(Long cartId);
    Cart getCartByUser(Long userId);
    void clearCart(Long cartId);
    double getTotalAmount(Long cartId);
    CartDto convertToDto(Cart cart);
}
