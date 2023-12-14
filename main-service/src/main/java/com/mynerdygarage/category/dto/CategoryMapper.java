package com.mynerdygarage.category.dto;

import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static CategoryFullDto categoryToFullDto(Category category) {

        return new CategoryFullDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                UserMapper.userToShortDto(category.getCreator())
        );
    }

    public static List<CategoryFullDto> categoryToFullDto(Iterable<Category> categories) {

        List<CategoryFullDto> toReturn = new ArrayList<>();

        for (Category category : categories) {
            toReturn.add(categoryToFullDto(category));
        }

        return toReturn;
    }

    public static CategoryShortDto categoryToShortDto(Category category) {

        return new CategoryShortDto(category.getId(), category.getName());
    }

    public static List<CategoryShortDto> categoryToShortDto(Iterable<Category> categories) {

        List<CategoryShortDto> toReturn = new ArrayList<>();

        for (Category category : categories) {
            toReturn.add(categoryToShortDto(category));
        }

        return toReturn;
    }
}
