package com.onlinestore.buy.requests;

import com.onlinestore.buy.entities.Category;
import lombok.Data;

@Data
public class AddProductRequest {
    private String name;
    private String brand;
    private String description;
    private double price;
    private int inventory;
    private Category category;
}
