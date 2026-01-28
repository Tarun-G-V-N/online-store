package com.onlinestore.buy.controllers;

import com.onlinestore.buy.entities.Cart;
import com.onlinestore.buy.entities.CartItem;
import com.onlinestore.buy.entities.User;
import com.onlinestore.buy.responses.APIResponse;
import com.onlinestore.buy.services.ICartItemService;
import com.onlinestore.buy.services.ICartService;
import com.onlinestore.buy.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}/cartItems")
@RequiredArgsConstructor
public class CartItemController {
    private final ICartItemService cartItemService;
    private final IUserService userService;
    private final ICartService cartService;

    @PostMapping("/item/add")
    public ResponseEntity<APIResponse> addItemToCart(@RequestParam Long productId, @RequestParam int quantity) {
        User user = userService.getAuthenticatedUser();
        Cart cart = cartService.initializeNewCartForUser(user);
         CartItem cartItem = cartItemService.addItemToCart(cart.getId(), productId, quantity);
        return ResponseEntity.ok(new APIResponse("Item added to cart successfully", cartItemService.convertToDto(cartItem)));
    }

    @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
    public ResponseEntity<APIResponse> removeItemFromCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
        cartItemService.removeItemFromCart(cartId, productId);
        return ResponseEntity.ok(new APIResponse("Item removed from cart successfully", null));
    }

    @PutMapping("/cart/{cartId}/item/{productId}/update")
    public ResponseEntity<APIResponse> updateCartItemQuantity(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId, @RequestParam int quantity) {
        cartItemService.updateQuantity(cartId, productId, quantity);
        return ResponseEntity.ok(new APIResponse("Item quantity updated successfully", null));
    }
}
