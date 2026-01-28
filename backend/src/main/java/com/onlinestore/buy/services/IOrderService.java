package com.onlinestore.buy.services;

import com.onlinestore.buy.dtos.OrderDto;
import com.onlinestore.buy.entities.Order;
import com.onlinestore.buy.requests.PaymentRequest;
import com.stripe.exception.StripeException;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);

    String createPaymentIntent(PaymentRequest request) throws StripeException;

    List<Order> getOrdersByUserId(Long userId);
    OrderDto convertToDto(Order order);
    List<OrderDto> convertToDtoList(List<Order> orders);
}
