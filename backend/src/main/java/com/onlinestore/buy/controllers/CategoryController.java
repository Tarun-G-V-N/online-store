package com.onlinestore.buy.controllers;

import com.onlinestore.buy.entities.Category;
import com.onlinestore.buy.responses.APIResponse;
import com.onlinestore.buy.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.version}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addCategory(@RequestBody Category category){
        Category categoryResponse = categoryService.addCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse("Category added successfully", categoryResponse));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse> getAllCategories(){
        List<Category> categoryResponse = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Categories fetched successfully", categoryResponse));
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<APIResponse> getCategoryById(@PathVariable("id") Long id){
        Category categoryResponse = categoryService.getCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Category fetched successfully", categoryResponse));

    }

    @GetMapping("/category/{name}/category")
    public ResponseEntity<APIResponse> searchCategoryByName(@PathVariable("name") String name){
        Category categoryResponse = categoryService.getCategoryByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Categories fetched successfully", categoryResponse));
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<APIResponse> updateCategory(@PathVariable("id") Long id, @RequestBody Category category){
        Category categoryResponse = categoryService.updateCategory(category, id);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Category updated successfully", categoryResponse));
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<APIResponse> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Category deleted successfully", null));
    }
}
