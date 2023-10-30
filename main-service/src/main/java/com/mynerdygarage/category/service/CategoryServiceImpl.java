package com.mynerdygarage.category.service;

import com.mynerdygarage.category.dto.CategoryFullDto;
import com.mynerdygarage.category.dto.CategoryMapper;
import com.mynerdygarage.category.dto.NewCategoryDto;
import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.category.repository.CategoryRepository;
import com.mynerdygarage.category.service.util.CategoryChecker;
import com.mynerdygarage.category.service.util.CategoryCreator;
import com.mynerdygarage.category.service.util.CategoryUpdater;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    @Override
    public CategoryFullDto addCategory(Long creatorId, NewCategoryDto newCategoryDto) {

        log.info("-- Saving category by user with Id={}: {}", creatorId, newCategoryDto);

        User creator = userRepository.findById(creatorId).orElseThrow(() ->
                new NotFoundException("- CreatorId not found: " + creatorId));

        Category category = CategoryCreator.create(newCategoryDto, creator);

        CategoryChecker.check(categoryRepository, creatorId, CategoryMapper.categoryToFullDto(category));

        CategoryFullDto fullDtoToReturn = CategoryMapper.categoryToFullDto(categoryRepository.save(category));

        log.info("-- Category has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public CategoryFullDto update(Long creatorId, Long categoryId, CategoryFullDto inputDto) {

        log.info("-- Updating vehicle by vehicleIdId={}: {}", creatorId, inputDto);

        if (!userRepository.existsById(creatorId)) {
            throw new NotFoundException("- CreatorId not found: " + creatorId);
        }

        CategoryChecker.check(categoryRepository, creatorId, inputDto);

        Category categoryToUpdate = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("- CategoryId not found: " + categoryId));

        CategoryUpdater.update(categoryToUpdate, inputDto);

        CategoryFullDto fullDtoToReturn = CategoryMapper.categoryToFullDto(categoryRepository.save(categoryToUpdate));

        log.info("-- Category has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public List<CategoryFullDto> getDefaultCategories() {

        log.info("-- Getting default categories");

        List<CategoryFullDto> listToReturn =
                CategoryMapper.categoryToFullDto(categoryRepository.findByCreatorId(null));

        log.info("-- Default categories list returned, size={}", listToReturn.size());

        return listToReturn;
    }

    @Override
    public CategoryFullDto getById(Long creatorId, Long categoryId) {

        log.info("-- Getting category by categoryId={}", categoryId);

        if (!userRepository.existsById(creatorId)) {
            throw new NotFoundException("- CreatorId not found: " + creatorId);
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("- CategoryId not found: " + categoryId));

        CategoryFullDto dtoToReturn =
                CategoryMapper.categoryToFullDto(category);

        log.info("-- Category returned:{}", dtoToReturn);

        return dtoToReturn;
    }

    @Override
    public List<CategoryFullDto> getCustomCategoriesByCreatorId(Long creatorId) {

        log.info("-- Getting custom categories by creatorId:{}", creatorId);

        if (!userRepository.existsById(creatorId)) {
            throw new NotFoundException("- CreatorId not found: " + creatorId);
        }

        List<CategoryFullDto> listToReturn =
                CategoryMapper.categoryToFullDto(categoryRepository.findByCreatorId(creatorId));

        log.info("-- Custom categories list returned, size={}", listToReturn.size());

        return listToReturn;
    }

    @Override
    public List<CategoryFullDto> getAvailableCategoriesByUserId(Long userId) {

        log.info("-- Getting categories by userId:{}", userId);

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("- UserId not found: " + userId);
        }

        List<Long> ids = new ArrayList<>(List.of(userId));
        ids.add(null);

        List<CategoryFullDto> listToReturn =
                CategoryMapper.categoryToFullDto(categoryRepository.findByCreatorIdIn(ids));

        log.info("-- Categories list returned, size={}", listToReturn.size());

        return listToReturn;
    }

    @Override
    public void removeById(Long creatorId, Long categoryId) {

        log.info("--- Deleting category by categoryId={}", categoryId);

        if (!userRepository.existsById(creatorId)) {
            throw new NotFoundException("- CreatorId not found: " + creatorId);
        }

        Category categoryToCheck = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("- CategoryId not found: " + categoryId));

        CategoryFullDto dtoToShowInLog = CategoryMapper.categoryToFullDto(categoryToCheck);

        categoryRepository.deleteById(categoryId);

        log.info("--- Category deleted: {}", dtoToShowInLog);
    }
}
