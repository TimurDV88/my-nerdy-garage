package com.mynerdygarage.comment.service;

import com.mynerdygarage.comment.dto.CommentFullDto;
import com.mynerdygarage.comment.dto.CommentMapper;
import com.mynerdygarage.comment.dto.NewCommentDto;
import com.mynerdygarage.comment.model.Comment;
import com.mynerdygarage.comment.repository.CommentRepository;
import com.mynerdygarage.comment.service.util.CommentChecker;
import com.mynerdygarage.comment.service.util.CommentCreator;
import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.util.NullChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl {

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    @Transactional
    //@Override
    public CommentFullDto addComment(
            Long authorId, String commentObjectClass, Long commentObjectId, NewCommentDto newCommentDto) {

        log.info("-- Saving comment to object={} by user with Id={}: {}", commentObjectClass, authorId, newCommentDto);

        User user = userRepository.findById(authorId).orElseThrow(() ->
                new NotFoundException("- authorId not found: " + authorId));

        Comment comment =
                CommentCreator.createFromNewDto(user.getId(), commentObjectClass, commentObjectId, newCommentDto);

        CommentChecker.checkNewComment(commentRepository, comment);

        CommentFullDto fullDtoToReturn = CommentMapper.modelToFullDto(commentRepository.save(comment));

        log.info("-- Comment has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Transactional
    //@Override
    public CommentFullDto update(Long userId, Long commentId, NewCommentDto inputDto) {

        log.info("-- Updating comment by commentId={}: {}", commentId, inputDto);

        Comment commentToUpdate = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("- commentId not found: " + commentId));

        if (!userId.equals(commentToUpdate.getAuthorId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not author of comment with id=" + commentId);
        }

        Comment updatedComment = commentToUpdate;

        NullChecker.setIfNotNull(updatedComment::setTitle, (inputDto.getTitle()));
        NullChecker.setIfNotNull(updatedComment::setContent, (inputDto.getContent()));

        CommentChecker.checkNewComment(commentRepository, updatedComment);

        CommentFullDto fullDtoToReturn = CommentMapper.modelToFullDto(commentRepository.save(updatedComment));

        log.info("-- Work has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }
}
