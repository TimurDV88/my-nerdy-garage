package com.mynerdygarage.comment.repository;

import com.mynerdygarage.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(""" 
            SELECT COUNT (c.id) = 1
            FROM Comment AS c
            WHERE c.authorId = :authorId
            AND c.commentObjectClass = :objectClass
            AND c.commentObjectId = :objectId
            AND c.dateTime BETWEEN :startTime AND :endTime
                        
            """)
    boolean existByAuthorIdAndObjectAndDateTimeInRange(
            Long authorId, String objectClass, Long objectId, LocalDateTime startTime, LocalDateTime endTime);
}
