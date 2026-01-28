package com.onlinestore.buy.dtos;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private double price;
}
