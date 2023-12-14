package com.mynerdygarage.category.service.util;

import com.mynerdygarage.category.dto.CategoryFullDto;
import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.category.repository.CategoryRepository;
import com.mynerdygarage.error.exception.ConflictOnRequestException;

public class CategoryChecker {

    private static void check(CategoryRepository categoryRepository, Long creatorId, String name) {

        if (categoryRepository.existsByCreatorIdAndNameIgnoreCase(creatorId, name)) {
            throw new ConflictOnRequestException("- Category with this Name already created by this user");
        }

        if (categoryRepository.existsByCreatorIdAndNameIgnoreCase(null, name)) {
            throw new ConflictOnRequestException("- Category with this Name is default");
        }
    }

    public static void checkNewCategory(CategoryRepository categoryRepository, Long creatorId, Category category) {

        check(categoryRepository, creatorId, category.getName());
    }

    public static void checkUpdateCategory(CategoryRepository categoryRepository, Long creatorId,
                                           CategoryFullDto inputDto, Category categoryToUpdate) {

        if (!categoryToUpdate.getCreator().getId().equals(creatorId)) {
            throw new ConflictOnRequestException(String.format(
                    "- User with id=%d is not creator of category with id=%d", creatorId, categoryToUpdate.getId()));
        }

        check(categoryRepository, creatorId, inputDto.getName());
    }
}
