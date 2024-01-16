package com.mynerdygarage.comment.service.util;

import com.mynerdygarage.comment.dto.NewCommentDto;
import com.mynerdygarage.comment.model.Comment;

import java.time.LocalDateTime;

public class CommentCreator {

    public static Comment createFromNewDto(
            Long authorId, String commentObjectClass, Long commentObjectId, NewCommentDto newCommentDto) {

        Comment comment = new Comment();

        comment.setAuthorId(authorId);
        comment.setCommentObjectClass(commentObjectClass);
        comment.setCommentObjectId(commentObjectId);
        comment.setTitle(newCommentDto.getTitle());
        comment.setContent(newCommentDto.getContent());
        comment.setDateTime(LocalDateTime.now());

        return comment;
    }
}
