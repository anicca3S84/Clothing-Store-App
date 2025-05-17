package com.dailycode.clothingstore.data;

import com.dailycode.clothingstore.model.Category;
import com.dailycode.clothingstore.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@Component
public class CategoryInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
//        createCategoriesIfNotExits();
    }

    private void createCategoriesIfNotExits() {
        Category category = new Category();
        category.setName("T-Shirt");
        categoryRepository.save(category);

        Category category1 = new Category();
        category1.setName("Shirt");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Pant");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setName("Dress");
        categoryRepository.save(category3);
    }
}
