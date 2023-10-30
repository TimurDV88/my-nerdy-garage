package com.mynerdygarage.category.service;

import com.mynerdygarage.category.dto.CategoryFullDto;
import com.mynerdygarage.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryFullDto addCategory(Long creatorId, NewCategoryDto newCategoryDto);

    CategoryFullDto update(Long creatorId, Long categoryId, CategoryFullDto inputDto);

    List<CategoryFullDto> getDefaultCategories();

    CategoryFullDto getById(Long creatorId, Long categoryId);

    List<CategoryFullDto> getCustomCategoriesByCreatorId(Long creatorId);

    List<CategoryFullDto> getAvailableCategoriesByUserId(Long userId);

    void removeById(Long creatorId, Long categoryId);
}
