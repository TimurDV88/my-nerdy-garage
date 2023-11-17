package com.mynerdygarage.category.dto;

import com.mynerdygarage.user.model.User;
import lombok.Data;

@Data
public class CategoryFullDto {

    private final Long id;

    private final String name;

    private final String description;

    private final User creator;
}
