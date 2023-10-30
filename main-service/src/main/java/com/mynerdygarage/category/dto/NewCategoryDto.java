package com.mynerdygarage.category.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewCategoryDto {

    @NotNull
    @NotBlank
    @Size(min = 2, message = "size must be between 2 and 20")
    @Size(max = 20, message = "size must be between 2 and 20")
    private final String name;

    @Size(min = 2, message = "size must be between 2 and 250")
    @Size(max = 20, message = "size must be between 2 and 250")
    private final String description;
}
