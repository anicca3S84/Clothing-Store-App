package com.dailycode.clothingstore.service.category;

import com.dailycode.clothingstore.exceptions.AlreadyExistException;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Category;
import com.dailycode.clothingstore.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new AlreadyExistException(category.getName() + " already exists"));
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        if(!category.getProducts().isEmpty()){
            throw new IllegalStateException("Cannot delete category. Please delete all related products first");
        }
        categoryRepository.delete(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.getCategoryByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
