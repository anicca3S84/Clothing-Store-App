package com.dailycode.clothingstore.controller;

import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Category;
import com.dailycode.clothingstore.response.ApiResponse;
import com.dailycode.clothingstore.service.category.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/categories/", produces = "application/json")
public class CategoryController {
    private final ICategoryService iCategoryService;

    public CategoryController(ICategoryService iCategoryService) {
        this.iCategoryService = iCategoryService;
    }

    @PostMapping("category/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        try {
            Category newCategory = iCategoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Add success", newCategory));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category,@PathVariable Long id){
        try {
            Category newCategory = iCategoryService.updateCategory(id, category);
            return ResponseEntity.ok(new ApiResponse("Update success", newCategory));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try {
            iCategoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("Delete success", null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("category/by/name")
    public ResponseEntity<ApiResponse> getCategoryByName(@RequestParam String name){
        try {
            Category category = iCategoryService.getCategoryByName(name);
            if (category == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Get success", category));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Get failed", null));
        }
    }

    @GetMapping("category/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category category = iCategoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Get success", category));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", null));
        }
    }

    @GetMapping("all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = iCategoryService.getAllCategories();
            if(categories == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Get success", categories));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
