package com.onlinestore.buy.services;

import com.onlinestore.buy.dtos.CartItemDto;
import com.onlinestore.buy.dtos.ProductDto;
import com.onlinestore.buy.entities.CartItem;
import com.onlinestore.buy.entities.Product;

public interface ICartItemService {
    CartItem addItemToCart(Long cartId, Long ProductId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateQuantity(Long cartId, Long productId, int quantity);
    CartItem getCartItem(Long cartId, Long productId);
    CartItemDto convertToDto(CartItem cartItem);
}
