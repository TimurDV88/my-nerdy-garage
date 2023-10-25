package com.mynerdygarage.int_test;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserIntegrationTest {

    private final UserController userController;
    private static NewUserDto properNewUserDto;
    private static String birthDateStr;

    @BeforeEach
    void setUp() {

        birthDateStr = "01.01.1991";
        properNewUserDto = new NewUserDto(
                "ProperUserName",
                "properUser@mail.com",
                birthDateStr);
    }

    @Test
    void shouldAddUser() {

        UserFullDto dtoToCheck = userController.add(properNewUserDto);

        assertEquals(properNewUserDto.getName(), dtoToCheck.getName());
        assertEquals(properNewUserDto.getEmail(), dtoToCheck.getEmail());
        assertEquals(properNewUserDto.getBirthDate(), dtoToCheck.getBirthDate());

        NewUserDto existedNameDto = new NewUserDto(
                "ProperUserName",
                "anotherUser@mail.com",
                birthDateStr);
        assertThrows(ConflictOnRequestException.class, () -> userController.add(existedNameDto));

        NewUserDto existedEmailDto = new NewUserDto(
                "AnotherUserName",
                "properUser@mail.com",
                birthDateStr);
        assertThrows(ConflictOnRequestException.class, () -> userController.add(existedEmailDto));

        NewUserDto futureBirthDateDto = new NewUserDto(
                "AnotherUserName",
                "AnotherUser@mail.com",
                "01.01.2050");
        assertThrows(IncorrectRequestException.class, () -> userController.add(futureBirthDateDto));

        NewUserDto wrongBirthDateDto = new NewUserDto(
                "AnotherUserName",
                "AnotherUser@mail.com",
                "01-01-2000");
        assertThrows(IncorrectRequestException.class, () -> userController.add(wrongBirthDateDto));
    }

    @Test
    void shouldUpdateUser() {

        UserFullDto dtoToCheck = userController.add(properNewUserDto);

        Long id = dtoToCheck.getId();

        String newName = "newName";
        UserFullDto updatedNameDto = new UserFullDto(null, newName, null, null, null);
        updatedNameDto = userController.update(id, updatedNameDto);

        assertEquals(newName, updatedNameDto.getName());
        assertEquals(properNewUserDto.getEmail(), updatedNameDto.getEmail());
        assertEquals(properNewUserDto.getBirthDate(), updatedNameDto.getBirthDate());

        UserFullDto sameNameDto = new UserFullDto(null, newName, null, null, null);
        assertThrows(ConflictOnRequestException.class, () -> userController.update(id, sameNameDto));

        String newEmail = "newEmail";
        UserFullDto updatedEmailDto = new UserFullDto(null, null, newEmail, null, null);
        updatedEmailDto = userController.update(id, updatedEmailDto);

        assertEquals(properNewUserDto.getName(), dtoToCheck.getName());
        assertEquals(newEmail, updatedEmailDto.getEmail());
        assertEquals(properNewUserDto.getBirthDate(), dtoToCheck.getBirthDate());

        UserFullDto sameEmailDto = new UserFullDto(null, null, newEmail, null, null);
        assertThrows(ConflictOnRequestException.class, () -> userController.update(id, sameEmailDto));
    }

    @Test
    void shouldGetUserById() {

        UserFullDto dtoToCheck = userController.add(properNewUserDto);

        Long id = dtoToCheck.getId();

        assertEquals(dtoToCheck, userController.getById(id));
    }

    @Test
    void shouldRemoveUserById() {

        UserFullDto dtoToCheck = userController.add(properNewUserDto);

        Long id = dtoToCheck.getId();

        assertEquals(dtoToCheck, userController.getById(id));

        userController.removeById(id);

        assertThrows(NotFoundException.class, () -> userController.getById(id));
    }
}