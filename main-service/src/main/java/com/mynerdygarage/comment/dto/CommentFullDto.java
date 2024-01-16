package com.mynerdygarage.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentFullDto {

    private final Long id;

    private final Long authorId;

    private final String commentObjectClass;

    private final Long commentObjectId;

    private final String title;

    private final String content;

    private final LocalDateTime dateTime;
}
