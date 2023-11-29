package com.mynerdygarage.category.service.util;

import com.mynerdygarage.category.dto.CategoryFullDto;
import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.util.NullChecker;

public class CategoryUpdater {

    public static Category update(Category categoryToUpdate, CategoryFullDto inputDto) {

        NullChecker.setIfNotNull(categoryToUpdate::setName, inputDto.getName());
        NullChecker.setIfNotNull(categoryToUpdate::setDescription, inputDto.getDescription());

        return categoryToUpdate;
    }
}
