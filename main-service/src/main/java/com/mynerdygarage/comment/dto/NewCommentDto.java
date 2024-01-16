package com.mynerdygarage.comment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewCommentDto {

    @NotNull
    @Size(min = 2, message = "size must be between 2 and 50")
    @Size(max = 50, message = "size must be between 2 and 50")
    private final String title;

    @NotNull
    @Size(min = 2, message = "size must be between 2 and 50")
    @Size(max = 500, message = "size must be between 2 and 500")
    private final String content;
}
