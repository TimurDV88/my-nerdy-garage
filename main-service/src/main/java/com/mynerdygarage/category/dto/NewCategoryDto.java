package com.mynerdygarage.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


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
