package com.mynerdygarage.category.repository;

import com.mynerdygarage.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByCreatorIdAndNameIgnoreCase(Long creatorId, String name);

    List<Category> findByCreatorId(Long creatorId);

    @Query("SELECT c " +
            "FROM Category AS c " +
            "WHERE c.creator.id = NULL " +
            "OR c.creator.id = :userId")
    List<Category> findAvailableCategoriesByUserId(Long userId);
}
