package com.mynerdygarage.comment.dto;

import com.mynerdygarage.comment.model.Comment;

import java.util.List;
import java.util.stream.StreamSupport;

public class CommentMapper {

    public static CommentFullDto modelToFullDto(Comment comment) {

        return CommentFullDto.builder()
                .id(comment.getId())
                .authorId(comment.getAuthorId())
                .commentObjectClass(comment.getCommentObjectClass())
                .commentObjectId(comment.getCommentObjectId())
                .title(comment.getTitle())
                .content(comment.getContent())
                .dateTime(comment.getDateTime())
                .build();
    }

    public static List<CommentFullDto> modelToFullDto(Iterable<Comment> comments) {

        return StreamSupport.stream(comments.spliterator(), false)
                .map(CommentMapper::modelToFullDto)
                .toList();
    }
}
