package com.onlinestore.buy.services;

import com.onlinestore.buy.dtos.ProductDto;
import com.onlinestore.buy.entities.Product;
import com.onlinestore.buy.requests.AddProductRequest;
import com.onlinestore.buy.requests.UpdateProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest addProductRequest);
    Product updateProduct(UpdateProductRequest updateProductRequest, Long id);
    void deleteProduct(Long id);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByCategoryId(Long categoryId);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByBrandAndName(String brand, String name);
    List<Product> getProductsByName(String name);
    List<Product> getDistinctProductsByName();
    List<String> getAllDistinctBrands();
    ProductDto convertToDto(Product product);
    List<ProductDto> convertToProductDtos(List<Product> products);
    List<Product> searchProductsByImage(MultipartFile image);
}
