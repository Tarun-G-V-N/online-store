package com.onlinestore.buy.controllers;

import com.onlinestore.buy.entities.Order;
import com.onlinestore.buy.requests.PaymentRequest;
import com.onlinestore.buy.responses.APIResponse;
import com.onlinestore.buy.services.IOrderService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.version}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/user/{userId}/place-order")
    public ResponseEntity<APIResponse> placeOrder(@PathVariable("userId") Long userId) {
        Order order = orderService.placeOrder(userId);
        return ResponseEntity.ok(new APIResponse("Order placed successfully", orderService.convertToDto(order)));
    }

    @GetMapping("/user/{userId}/orders")
    public ResponseEntity<APIResponse> getOrdersByUser(@PathVariable("userId") Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(new APIResponse("Orders retrieved successfully", orderService.convertToDtoList(orders)));
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) throws StripeException {
        String clientSecret = orderService.createPaymentIntent(request);
        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
    }
}
