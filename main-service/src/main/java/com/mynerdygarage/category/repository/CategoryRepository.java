package com.mynerdygarage.category.repository;

import com.mynerdygarage.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByCreatorIdAndNameIgnoreCase(Long creatorId, String name);

    List<Category> findByCreatorIdIn(List<Long> creatorIds);

    List<Category> findByCreatorId(Long creatorId);
}
