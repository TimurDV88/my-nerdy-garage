package com.mynerdygarage.category.dto;

import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.user.dto.UserMapper;

import java.util.List;
import java.util.stream.StreamSupport;

public class CategoryMapper {

    public static CategoryFullDto modelToFullDto(Category category) {

        return new CategoryFullDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                UserMapper.modelToShortDto(category.getCreator())
        );
    }

    public static List<CategoryFullDto> modelToFullDto(Iterable<Category> categories) {

        return StreamSupport.stream(categories.spliterator(), false)
                .map(CategoryMapper::modelToFullDto)
                .toList();
    }

    public static CategoryShortDto modelToShortDto(Category category) {

        return new CategoryShortDto(category.getId(), category.getName());
    }

    public static List<CategoryShortDto> modelToShortDto(Iterable<Category> categories) {

        return StreamSupport.stream(categories.spliterator(), false)
                .map(CategoryMapper::modelToShortDto)
                .toList();
    }
}
