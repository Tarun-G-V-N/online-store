package com.onlinestore.buy.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
    private double totalAmount;
}
