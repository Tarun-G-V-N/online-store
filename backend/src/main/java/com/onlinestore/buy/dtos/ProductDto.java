package com.onlinestore.buy.dtos;

import com.onlinestore.buy.entities.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private int inventory;
    private CategoryDto category;
    private List<ImageDto> images;
}
