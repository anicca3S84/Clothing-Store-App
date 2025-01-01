package com.dailycode.clothingstore.repository;

import com.dailycode.clothingstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    Category getCategoryByName(String name);

    boolean existsByName(String name);
}
