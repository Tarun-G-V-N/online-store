package com.onlinestore.buy.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CartItemDto {
    private Long id;
    private ProductDto product;
    private int quantity;
    private double totalPrice;
}
