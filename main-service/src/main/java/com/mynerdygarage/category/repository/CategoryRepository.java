package com.mynerdygarage.category.repository;

import com.mynerdygarage.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByCreatorIdAndNameIgnoreCase(Long creatorId, String name);

    List<Category> findByCreatorId(Long creatorId);

    @Query("SELECT c " +
            "FROM Category AS c " +
            "WHERE c.creator.id IS NULL " +
            "OR c.creator.id = :userId " +
            "ORDER BY c.id")
    List<Category> findAvailableCategoriesByUserId(Long userId);

    @Query("SELECT c.id " +
            "FROM Category AS c " +
            "WHERE c.creator.id = :creatorId " +
            "OR c.creator.id IS NULL")
    List<Long> findAvailableIdsByCreatorId(Long creatorId);
}
