package com.onlinestore.buy.controllers;

import com.onlinestore.buy.entities.Cart;
import com.onlinestore.buy.responses.APIResponse;
import com.onlinestore.buy.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}/carts")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;

    @GetMapping("/user/{userId}/cart")
    public ResponseEntity<APIResponse> getCartByUser(@PathVariable("userId") Long userId) {
        Cart cart = cartService.getCartByUser(userId);
        return ResponseEntity.ok(new APIResponse("Cart retrieved successfully", cartService.convertToDto(cart)));
    }

    @DeleteMapping("/cart/{cartId}/clear")
    public void clearCart(@PathVariable("cartId") Long cartId) {
        cartService.clearCart(cartId);
    }
}
