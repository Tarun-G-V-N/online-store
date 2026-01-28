package com.onlinestore.buy.services;

import com.onlinestore.buy.entities.Category;

import java.util.List;

public interface ICategoryService {
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategory(Long id);
    Category getCategory(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
}
