package com.mynerdygarage.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mynerdygarage.category.dto.CategoryFullDto;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.model.User;

import java.util.List;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.PathMappedEndpoints;
import org.springframework.boot.actuate.endpoint.web.servlet.AdditionalHealthEndpointPathsWebMvcHandlerMapping;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AITestForCategoryServiceImpl {
    @MockBean
    private AdditionalHealthEndpointPathsWebMvcHandlerMapping additionalHealthEndpointPathsWebMvcHandlerMapping;

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @MockBean
    private PathMappedEndpoints pathMappedEndpoints;

    @MockBean
    private WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping;

    /**
     * Method under test: {@link CategoryServiceImpl#update(Long, Long, CategoryFullDto)}
     */
    @Test
    void testUpdate() {
        assertThrows(NotFoundException.class, () -> categoryServiceImpl.update(1L, 1L,
                new CategoryFullDto(1L, "Name", "The characteristics of someone or something", new User())));
        assertThrows(NotFoundException.class, () -> categoryServiceImpl.update(2L, 1L,
                new CategoryFullDto(1L, "Name", "The characteristics of someone or something", new User())));
    }

    /**
     * Method under test: {@link CategoryServiceImpl#update(Long, Long, CategoryFullDto)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdate2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at com.mynerdygarage.category.service.util.CategoryChecker.check(CategoryChecker.java:11)
        //       at com.mynerdygarage.category.service.CategoryServiceImpl.update(CategoryServiceImpl.java:54)
        //   See https://diff.blue/R013 to resolve this issue.

        categoryServiceImpl.update(1L, 1L, null);
    }

    /**
     * Method under test: {@link CategoryServiceImpl#getDefaultCategories()}
     */
    @Test
    void testGetDefaultCategories() {
        List<CategoryFullDto> actualDefaultCategories = categoryServiceImpl.getDefaultCategories();
        assertEquals(9, actualDefaultCategories.size());
        CategoryFullDto getResult = actualDefaultCategories.get(5);
        assertEquals("CABIN", getResult.getName());
        CategoryFullDto getResult1 = actualDefaultCategories.get(6);
        assertEquals("ELECTRICITY", getResult1.getName());
        assertEquals(34L, getResult1.getId().longValue());
        assertEquals("All about electric works and spares", getResult1.getDescription());
        assertNull(getResult1.getCreator());
        assertEquals(33L, getResult.getId().longValue());
        assertEquals("All about cabin works and parts", getResult.getDescription());
        assertNull(getResult.getCreator());
        CategoryFullDto getResult2 = actualDefaultCategories.get(2);
        assertEquals(30L, getResult2.getId().longValue());
        String expectedDescription = String.join("", "All about bodywork - washing, detailing, doors, ",
                System.getProperty("sun.desktop"), " etc.");
        assertEquals(expectedDescription, getResult2.getDescription());
        assertNull(getResult2.getCreator());
        CategoryFullDto getResult3 = actualDefaultCategories.get(4);
        assertEquals(32L, getResult3.getId().longValue());
        assertEquals("All about brakes", getResult3.getDescription());
        assertNull(getResult3.getCreator());
        CategoryFullDto getResult4 = actualDefaultCategories.get(7);
        assertEquals(35L, getResult4.getId().longValue());
        assertEquals("Rims, tires etc.", getResult4.getDescription());
        assertNull(getResult4.getCreator());
        CategoryFullDto getResult5 = actualDefaultCategories.get(3);
        assertEquals(31L, getResult5.getId().longValue());
        assertEquals("Suspension, arms, stocks etc.", getResult5.getDescription());
        assertNull(getResult5.getCreator());
        CategoryFullDto getResult6 = actualDefaultCategories.get(1);
        assertEquals(29L, getResult6.getId().longValue());
        assertEquals("Gearbox oil, filters etc.", getResult6.getDescription());
        assertNull(getResult6.getCreator());
        CategoryFullDto getResult7 = actualDefaultCategories.get(8);
        assertEquals(36L, getResult7.getId().longValue());
        assertEquals("anything that doesn't match the existing categories", getResult7.getDescription());
        assertNull(getResult7.getCreator());
        CategoryFullDto getResult8 = actualDefaultCategories.get(0);
        assertEquals(28L, getResult8.getId().longValue());
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

    /**
     * Method under test: {@link CategoryServiceImpl#getById(Long, Long)}
     */
    @Test
    void testGetById() {
        assertThrows(NotFoundException.class, () -> categoryServiceImpl.getById(1L, 1L));
    }

    /**
     * Method under test: {@link CategoryServiceImpl#getCustomCategoriesByCreatorId(Long)}
     */
    @Test
    void testGetCustomCategoriesByCreatorId() {
        assertThrows(NotFoundException.class, () -> categoryServiceImpl.getCustomCategoriesByCreatorId(1L));
    }

    /**
     * Method under test: {@link CategoryServiceImpl#removeById(Long, Long)}
     */
    @Test
    void testRemoveById() {
        assertThrows(NotFoundException.class, () -> categoryServiceImpl.removeById(1L, 1L));
    }
}

