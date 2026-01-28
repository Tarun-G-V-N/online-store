package com.onlinestore.buy.dtos;

import com.onlinestore.buy.entities.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private double totalAmount;
    private LocalDateTime orderDate;
    private List<OrderItemDto> items;
}
