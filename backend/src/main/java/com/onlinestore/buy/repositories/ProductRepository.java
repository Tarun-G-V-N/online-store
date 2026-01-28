package com.onlinestore.buy.repositories;

import com.onlinestore.buy.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String category);
    List<Product> findAllByCategoryId(Long categoryId);
    List<Product> findByBrand(String brand);
    List<Product> findByBrandAndName(String brand, String name);
    List<Product> findByCategoryNameAndBrand(String category, String brand);
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByName(String name);
    boolean existsByNameAndBrand(String name, String brand);
}
