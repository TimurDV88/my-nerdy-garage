package com.mynerdygarage.comment.service.util;

import com.mynerdygarage.comment.model.Comment;
import com.mynerdygarage.comment.repository.CommentRepository;
import com.mynerdygarage.error.exception.ConflictOnRequestException;

public class CommentChecker {

    private final static int TIME_RANGE = 5;

    public static void checkNewComment(CommentRepository commentRepository, Comment comment) {

        if (commentRepository.existByAuthorIdAndObjectAndDateTimeInRange(
                comment.getAuthorId(),
                comment.getCommentObjectClass(),
                comment.getCommentObjectId(),
                comment.getDateTime().minusMinutes(TIME_RANGE),
                comment.getDateTime())) {

            throw new ConflictOnRequestException(
                    String.format("- Only one comment per %d min allowed", TIME_RANGE));
        }
    }
}
