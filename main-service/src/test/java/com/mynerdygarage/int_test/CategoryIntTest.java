package com.mynerdygarage.int_test;

import com.mynerdygarage.category.controller.CategoryController;
import com.mynerdygarage.category.dto.CategoryFullDto;
import com.mynerdygarage.category.dto.NewCategoryDto;
import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.controller.UserController;
import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryIntTest {

    private final UserController userController;

    private final CategoryController categoryController;

    private static Long creatorId;

    private static NewCategoryDto properNewCategoryDto;


    @BeforeEach
    void setUp() {

        String birthDateStr = "01.01.1991";
        NewUserDto properNewUserDto = new NewUserDto(
                "ProperUserName",
                "properUser@mail.com",
                birthDateStr);

        UserFullDto userFullDto = userController.add(properNewUserDto);
        creatorId = userFullDto.getId();

        properNewCategoryDto = new NewCategoryDto("Custom category N1", "Descr for custom cat N1");
    }

    @Test
    void shouldAddCategory() {

        CategoryFullDto categoryFullDtoToCheck = categoryController.addCategory(creatorId, properNewCategoryDto);

        assertEquals(properNewCategoryDto.getName(), categoryFullDtoToCheck.getName());

        // add with same name
        assertThrows(ConflictOnRequestException.class, () ->
                categoryController.addCategory(creatorId, properNewCategoryDto));
    }

    @Test
    void shouldUpdateCategory() {

        CategoryFullDto categoryFullDtoToCheck = categoryController.addCategory(creatorId, properNewCategoryDto);
        Long categoryId = categoryFullDtoToCheck.getId();

        assertEquals(properNewCategoryDto.getName(), categoryFullDtoToCheck.getName());

        String updatedName = "updated Name";

        CategoryFullDto dtoWithUpdatedName = new CategoryFullDto(null, updatedName, null, null);

        dtoWithUpdatedName = categoryController.update(creatorId, categoryId, dtoWithUpdatedName);

        assertEquals(updatedName, dtoWithUpdatedName.getName());

        // add with same name
        CategoryFullDto dtoWithSameName = dtoWithUpdatedName;
        assertThrows(ConflictOnRequestException.class, () ->
                categoryController.update(creatorId, categoryId, dtoWithSameName));

        // add with default name
        updatedName = "ENGINE";
        CategoryFullDto dtoWithDefaultName = new CategoryFullDto(null, updatedName, null, null);
        assertThrows(ConflictOnRequestException.class, () ->
                categoryController.update(creatorId, categoryId, dtoWithDefaultName));
    }

    @Test
    void shouldGetDefaultCategories() {

        // AI-generated
        List<CategoryFullDto> actualDefaultCategories = categoryController.getDefaultCategories(creatorId);
        assertEquals(9, actualDefaultCategories.size());
        CategoryFullDto getResult = actualDefaultCategories.get(5);
        assertEquals("CABIN", getResult.getName());
        CategoryFullDto getResult1 = actualDefaultCategories.get(6);
        assertEquals("ELECTRICITY", getResult1.getName());
        assertEquals("All about electric works and spares", getResult1.getDescription());
        assertNull(getResult1.getCreator());
        assertEquals("All about cabin works and parts", getResult.getDescription());
        assertNull(getResult.getCreator());
        CategoryFullDto getResult2 = actualDefaultCategories.get(2);
        String expectedDescription = String.join("", "All about bodywork - washing, detailing, doors, ",
                System.getProperty("sun.desktop"), " etc.");
        assertEquals(expectedDescription, getResult2.getDescription());
        assertNull(getResult2.getCreator());
        CategoryFullDto getResult3 = actualDefaultCategories.get(4);
        assertEquals("All about brakes", getResult3.getDescription());
        assertNull(getResult3.getCreator());
        CategoryFullDto getResult4 = actualDefaultCategories.get(7);
        assertEquals("Rims, tires etc.", getResult4.getDescription());
        assertNull(getResult4.getCreator());
        CategoryFullDto getResult5 = actualDefaultCategories.get(3);
        assertEquals("Suspension, arms, stocks etc.", getResult5.getDescription());
        assertNull(getResult5.getCreator());
        CategoryFullDto getResult6 = actualDefaultCategories.get(1);
        assertEquals("Gearbox oil, filters etc.", getResult6.getDescription());
        assertNull(getResult6.getCreator());
        CategoryFullDto getResult7 = actualDefaultCategories.get(8);
        assertEquals("anything that doesn't match the existing categories", getResult7.getDescription());
        assertNull(getResult7.getCreator());
        CategoryFullDto getResult8 = actualDefaultCategories.get(0);
        assertEquals("Engine oil, oil filter, spark plugs etc.", getResult8.getDescription());
        assertNull(getResult8.getCreator());
        assertEquals("ENGINE", getResult8.getName());
        assertEquals("BRAKES", getResult3.getName());
        assertEquals("WHEELS", getResult4.getName());
        assertEquals("GEARBOX", getResult6.getName());
        assertEquals("OTHER", getResult7.getName());
        assertEquals("BODY", getResult2.getName());
        assertEquals("CHASSIS", getResult5.getName());
    }

    @Test
    void shouldGetById() {

        CategoryFullDto categoryFullDtoToCheck = categoryController.addCategory(creatorId, properNewCategoryDto);
        Long categoryId = categoryFullDtoToCheck.getId();

        assertEquals(categoryFullDtoToCheck, categoryController.getById(creatorId, categoryId));

        assertThrows(NotFoundException.class, () ->
                categoryController.getById(999L, categoryId));

        assertThrows(NotFoundException.class, () ->
                categoryController.getById(creatorId, 999L));
    }

    @Test
    void shouldGetCustomCategoriesByCreatorId() {

        CategoryFullDto categoryFullDtoToCheck = categoryController.addCategory(creatorId, properNewCategoryDto);
        Long categoryId = categoryFullDtoToCheck.getId();

        assertEquals(categoryController.getById(creatorId, categoryId),
                categoryController.getCustomCategoriesByCreatorId(creatorId).get(0));
    }

    @Test
    void shouldGetAvailableCategoriesByCreatorId() {

        CategoryFullDto categoryFullDtoToCheck = categoryController.addCategory(creatorId, properNewCategoryDto);
        Long categoryId = categoryFullDtoToCheck.getId();

        List<CategoryFullDto> actualDefaultCategories = categoryController.getDefaultCategories(creatorId);
        int sizeOfDefaultList = actualDefaultCategories.size();
        CategoryFullDto firstDefaultCategory = actualDefaultCategories.get(0);

        List<CategoryFullDto> actualAvailableCategories = categoryController.getAvailableCategoriesByUserId(creatorId);
        int sizeOfAvailableList = actualAvailableCategories.size();

        for (CategoryFullDto categoryFullDto : actualAvailableCategories) {
            System.out.println(categoryFullDto);
        }

        assertEquals(categoryController.getById(creatorId, categoryId),
                actualAvailableCategories.get(sizeOfAvailableList - 1));

        assertEquals(firstDefaultCategory,
                actualAvailableCategories.get(0));
    }
}
