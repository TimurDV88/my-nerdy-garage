package com.mynerdygarage.category.service.util;

import com.mynerdygarage.category.dafault.DefaultCategoriesHandler;
import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.category.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DefaultCategoryLoader implements ApplicationRunner {

    private final CategoryRepository categoryRepository;

    @Autowired
    public DefaultCategoryLoader(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(ApplicationArguments args) {

        setDefaultCategories();
    }

    private void setDefaultCategories() {

        log.info("-- Setting default Categories");

        List<Category> defaultCategories = DefaultCategoriesHandler.getDefaultCategories();

        List<String> dbDefaultCategories = categoryRepository.findByCreatorId(null).stream()
                .map(Category::getName).toList();

        for (Category category : defaultCategories) {

            if (!dbDefaultCategories.contains(category.getName())) {
                categoryRepository.save(category);
                log.info("--- Added default category:{}", category.getName());
            }
        }

        log.info("-- Default Categories List Size = {}", defaultCategories.size());
    }
}
