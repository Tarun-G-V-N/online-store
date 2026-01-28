package com.onlinestore.buy.services.impl;

import com.onlinestore.buy.dtos.CartItemDto;
import com.onlinestore.buy.entities.Cart;
import com.onlinestore.buy.entities.CartItem;
import com.onlinestore.buy.entities.Product;
import com.onlinestore.buy.repositories.CartItemRepository;
import com.onlinestore.buy.repositories.CartRepository;
import com.onlinestore.buy.services.ICartItemService;
import com.onlinestore.buy.services.ICartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final ICartService cartService;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    @Override
    public CartItem addItemToCart(Long cartId, Long ProductId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(ProductId);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(ProductId))
                .findFirst()
                .orElse(new CartItem());
        if (cartItem.getId() != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        cartItem.setTotalPrice();
        cartItemRepository.save(cartItem);
        cart.addItem(cartItem);
        cartRepository.save(cart);
        return cartItem;
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItemToRemove = getCartItem(cartId, productId);
        cart.removeCartItem(cartItemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(cartItem.getProduct().getPrice());
        cartItem.setTotalPrice();
        cartItemRepository.save(cartItem);
        double newTotalAmount = cart.getCartItems().stream().mapToDouble(CartItem::getTotalPrice).sum();
        cart.setTotalAmount(newTotalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
    }

    @Override
    public CartItemDto convertToDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemDto.class);
    }
}
