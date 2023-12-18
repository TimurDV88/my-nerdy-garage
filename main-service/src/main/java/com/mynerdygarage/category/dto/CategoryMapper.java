package com.mynerdygarage.category.dto;

import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

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

        List<CategoryFullDto> toReturn = new ArrayList<>();

        for (Category category : categories) {
            toReturn.add(modelToFullDto(category));
        }

        return toReturn;
    }

    public static CategoryShortDto modelToShortDto(Category category) {

        return new CategoryShortDto(category.getId(), category.getName());
    }

    public static List<CategoryShortDto> modelToShortDto(Iterable<Category> categories) {

        List<CategoryShortDto> toReturn = new ArrayList<>();

        for (Category category : categories) {
            toReturn.add(modelToShortDto(category));
        }

        return toReturn;
    }
}
