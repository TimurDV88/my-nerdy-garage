package com.mynerdygarage.int_test;

import com.mynerdygarage.category.dafault.DefaultCategoriesHandler;
import com.mynerdygarage.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test-h2")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DefaultCategoryLoaderTest {

    private final CategoryRepository categoryRepository;

    @Test
    void shouldRun() {

        System.out.println("\n" + categoryRepository.findAll());

        System.out.println("\n" + DefaultCategoriesHandler.getDefaultCategories());

        assertEquals(categoryRepository.findAll().size(), DefaultCategoriesHandler.getDefaultCategories().size());
    }
}