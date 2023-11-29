package com.mynerdygarage.category.controller;

import com.mynerdygarage.category.dto.CategoryFullDto;
import com.mynerdygarage.category.dto.NewCategoryDto;
import com.mynerdygarage.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryFullDto addCategory(@RequestHeader("X-Sharer-User-Id") Long creatorId,
                                       @RequestBody @Valid NewCategoryDto newCategoryDto) {

        return categoryService.addCategory(creatorId, newCategoryDto);
    }

    @PatchMapping("/{categoryId}")
    public CategoryFullDto update(@RequestHeader("X-Sharer-User-Id") Long creatorId,
                                  @PathVariable @NotNull Long categoryId,
                                  @RequestBody @Valid CategoryFullDto inputDto) {

        return categoryService.update(creatorId, categoryId, inputDto);
    }

    @GetMapping("/default")
    public List<CategoryFullDto> getDefaultCategories(@RequestHeader("X-Sharer-User-Id") Long requesterId) {

        return categoryService.getDefaultCategories();
    }

    @GetMapping("/{categoryId}")
    public CategoryFullDto getById(@RequestHeader("X-Sharer-User-Id") Long creatorId,
                                   @PathVariable @NotNull Long categoryId) {

        return categoryService.getById(creatorId, categoryId);
    }

    @GetMapping("/custom")
    public List<CategoryFullDto> getCustomCategoriesByCreatorId(@RequestHeader("X-Sharer-User-Id") Long creatorId) {

        return categoryService.getCustomCategoriesByCreatorId(creatorId);
    }

    @GetMapping("/available")
    public List<CategoryFullDto> getAvailableCategoriesByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {

        return categoryService.getAvailableCategoriesByUserId(userId);
    }

    @DeleteMapping("/{categoryId}")
    public void removeById(@RequestHeader("X-Sharer-User-Id") Long creatorId,
                           @PathVariable @NotNull Long categoryId) {

        categoryService.removeById(creatorId, categoryId);
    }
}
