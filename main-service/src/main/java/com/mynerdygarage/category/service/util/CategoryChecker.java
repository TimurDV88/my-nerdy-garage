package com.mynerdygarage.category.service.util;

import com.mynerdygarage.category.dto.CategoryFullDto;
import com.mynerdygarage.category.repository.CategoryRepository;
import com.mynerdygarage.error.exception.ConflictOnRequestException;

public class CategoryChecker {

    public static void check(CategoryRepository categoryRepository, Long creatorId, CategoryFullDto categoryDto) {

        if (categoryRepository.existsByCreatorIdAndNameIgnoreCase(creatorId, categoryDto.getName())) {
            throw new ConflictOnRequestException("- Category with this Name already created by this user");
        }

        if (categoryRepository.existsByCreatorIdAndNameIgnoreCase(null, categoryDto.getName())) {
            throw new ConflictOnRequestException("- Category with this Name is default");
        }
    }
}
