package com.onlinestore.buy.controllers;

import com.onlinestore.buy.dtos.ProductDto;
import com.onlinestore.buy.entities.Product;
import com.onlinestore.buy.requests.AddProductRequest;
import com.onlinestore.buy.requests.UpdateProductRequest;
import com.onlinestore.buy.responses.APIResponse;
import com.onlinestore.buy.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.version}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<APIResponse> addProduct(@RequestBody AddProductRequest addProductRequest){
        Product productResponse = productService.addProduct(addProductRequest);
        ProductDto productDto = productService.convertToDto(productResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse("Product added successfully", productDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{id}/update")
    public ResponseEntity<APIResponse> updateProduct(@PathVariable("id") Long id, @RequestBody UpdateProductRequest updateProductRequest){
        Product productResponse = productService.updateProduct(updateProductRequest, id);
        ProductDto productDto = productService.convertToDto(productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Product updated successfully", productDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<APIResponse> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Product deleted successfully", id));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllProducts(){
        List<Product> productResponse = productService.getAllProducts();
        List<ProductDto> productDtos = productService.convertToProductDtos(productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Products fetched successfully", productDtos));
    }

    @GetMapping("/product/{id}/product")
    public ResponseEntity<APIResponse> getProductById(@PathVariable("id") Long id){
        Product productResponse = productService.getProductById(id);
        ProductDto productDto = productService.convertToDto(productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Product fetched successfully", productDto));
    }

    @GetMapping("/product/name/{name}/product")
    public ResponseEntity<APIResponse> getProductsByName(@PathVariable("name") String name){
        List<Product> productResponse = productService.getProductsByName(name);
        List<ProductDto> productDtos = productService.convertToProductDtos(productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Product fetched successfully", productDtos));
    }

    @GetMapping("/{category}/products")
    public ResponseEntity<APIResponse> getProductsByCategory(@PathVariable("category") String name){
        List<Product> productResponse = productService.getProductsByCategory(name);
        List<ProductDto> productDtos = productService.convertToProductDtos(productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Products fetched successfully", productDtos));
    }

    @GetMapping("/category/{categoryId}/products")
    public ResponseEntity<APIResponse> getProductsByCategoryId(@PathVariable("categoryId") Long categoryId){
        List<Product> productResponse = productService.getProductsByCategoryId(categoryId);
        List<ProductDto> productDtos = productService.convertToProductDtos(productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Products fetched successfully", productDtos));
    }

    @GetMapping("/by/category-and-brand")
    public ResponseEntity<APIResponse> getProductsByCategoryAndBrand(@RequestParam String name, @RequestParam String brand){
        List<Product> productResponse = productService.getProductsByCategoryAndBrand(name, brand);
        List<ProductDto> productDtos = productService.convertToProductDtos(productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Product fetched successfully", productDtos));
    }

    @GetMapping("/by/brand")
    public ResponseEntity<APIResponse> getProductsByBrand(@RequestParam String brand){
        List<Product> productResponse = productService.getProductsByBrand(brand);
        List<ProductDto> productDtos = productService.convertToProductDtos(productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Product fetched successfully", productDtos));
    }

    @GetMapping("/by/brand-and-name")
    public ResponseEntity<APIResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name){
        List<Product> productResponse = productService.getProductsByBrandAndName(brand, name);
        List<ProductDto> productDtos = productService.convertToProductDtos(productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Product fetched successfully", productDtos));
    }

    @GetMapping("/distinct/products")
    public ResponseEntity<APIResponse> getDistinctProductsByName(){
        List<Product> productResponse = productService.getDistinctProductsByName();
        List<ProductDto> productDtos = productService.convertToProductDtos(productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Products fetched successfully", productDtos));
    }

    @GetMapping("/distinct/brands")
    public ResponseEntity<APIResponse> getAllDistinctBrands(){
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Brands fetched successfully", productService.getAllDistinctBrands()));
    }

    @PostMapping("/search-by-image")
    public ResponseEntity<APIResponse> searchProductsByImage(@RequestParam MultipartFile image){
        List<Product> matchedProducts = productService.searchProductsByImage(image);
        log.info("Found {} products", matchedProducts.size());
        List<ProductDto> productDtos = productService.convertToProductDtos(matchedProducts);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Products fetched successfully", productDtos));
    }
}
