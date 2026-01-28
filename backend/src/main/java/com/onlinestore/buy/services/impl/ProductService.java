package com.onlinestore.buy.services.impl;

import com.onlinestore.buy.dtos.ImageDto;
import com.onlinestore.buy.dtos.ProductDto;
import com.onlinestore.buy.entities.*;
import com.onlinestore.buy.repositories.*;
import com.onlinestore.buy.requests.AddProductRequest;
import com.onlinestore.buy.requests.UpdateProductRequest;
import com.onlinestore.buy.services.IProductService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ImageRepository imageRepository;
    private final ChromaService chromaService;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest addProductRequest) {
        if (isProductExists(addProductRequest.getName(), addProductRequest.getBrand())) throw new EntityExistsException(addProductRequest.getName() +" already exists.");
        Category category = categoryRepository.findByName(addProductRequest.getCategory().getName()).orElseGet(
                () -> categoryRepository.save(new Category(addProductRequest.getCategory().getName())));
        addProductRequest.setCategory(category);
        return productRepository.save(createProduct(addProductRequest));
    }

    private boolean isProductExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest addProductRequest) {
        return new Product(addProductRequest.getName(),
                addProductRequest.getBrand(), addProductRequest.getDescription(),
                addProductRequest.getPrice(), addProductRequest.getInventory(), addProductRequest.getCategory());
    }

    @Override
    public Product updateProduct(UpdateProductRequest updateProductRequest, Long id) {
        return productRepository.findById(id)
                .map(existingProduct -> updateExistingProduct(existingProduct, updateProductRequest))
                .map(productRepository::save)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest updateProductRequest) {
        existingProduct.setName(updateProductRequest.getName());
        existingProduct.setBrand(updateProductRequest.getBrand());
        existingProduct.setDescription(updateProductRequest.getDescription());
        existingProduct.setPrice(updateProductRequest.getPrice());
        existingProduct.setInventory(updateProductRequest.getInventory());
        Category category = categoryRepository.findByName(updateProductRequest.getCategory().getName()).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresentOrElse(product ->
                {
                    List<CartItem> cartItems = cartItemRepository.findByProductId(id);
                    cartItems.forEach(cartItem -> {
                        Cart cart = cartItem.getCart();
                        cart.removeCartItem(cartItem);
                        cartItemRepository.delete(cartItem);
                    }
                );

                    List<OrderItem> orderItems = orderItemRepository.findByProductId(id);
                    orderItems.forEach(orderItem -> {
                        Order order = orderItem.getOrder();
                        order.removeOrderItem(orderItem);
                        orderItemRepository.delete(orderItem);
                    }
                );
                    Optional.ofNullable(product.getCategory()).ifPresent(category -> category.getProducts().remove(product));
                    product.setCategory(null);
                    productRepository.deleteById(id);
                },
                () -> {
                    throw new EntityNotFoundException("Product not found");
                }
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getDistinctProductsByName() {
        List<Product> products = getAllProducts();
        Map<String, Product> distinctProducts = products.stream()
                .collect(Collectors.toMap(Product :: getName, product -> product,
                        (existing, replacement) -> existing));
        return new ArrayList<>(distinctProducts.values());
    }

    @Override
    public List<String> getAllDistinctBrands() {
        return productRepository.findAll().stream().map(Product::getBrand).distinct().toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }

    @Override
    public List<ProductDto> convertToProductDtos(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public List<Product> searchProductsByImage(MultipartFile image) {
        try {
            List<Long> productIds =  chromaService.searchImageSimilarity(image);
            return productRepository.findAllById(productIds);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
