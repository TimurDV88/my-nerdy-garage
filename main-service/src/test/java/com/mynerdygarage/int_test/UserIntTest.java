package com.mynerdygarage.int_test;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.controller.UserController;
import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.dto.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test-postgres")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserIntTest {

    private final UserController userController;
    private static NewUserDto properNewUserDto;
    private static String birthDateStr;

    @BeforeEach
    void setUp() {

        birthDateStr = "01.01.1991";
        properNewUserDto = new NewUserDto(
                "ProperUserName",
                "properUser@mail.com",
                birthDateStr,
                "password",
                "password");
    }

    @Test
    void shouldAddUser() {

        UserFullDto dtoToCheck = userController.registerNewUser(properNewUserDto);

        assertEquals(properNewUserDto.getName(), dtoToCheck.getName());
        assertEquals(properNewUserDto.getEmail(), dtoToCheck.getEmail());
        assertEquals(properNewUserDto.getBirthDate(), dtoToCheck.getBirthDate());

        NewUserDto existedNameDto = new NewUserDto(
                "ProperUserName",
                "anotherUser@mail.com",
                birthDateStr,
                "password",
                "password");
        assertThrows(ConflictOnRequestException.class, () -> userController.registerNewUser(existedNameDto));

        NewUserDto existedEmailDto = new NewUserDto(
                "AnotherUserName",
                "properUser@mail.com",
                birthDateStr,
                "password",
                "password");
        assertThrows(ConflictOnRequestException.class, () -> userController.registerNewUser(existedEmailDto));

        NewUserDto futureBirthDateDto = new NewUserDto(
                "AnotherUserName",
                "AnotherUser@mail.com",
                "01.01.2050",
                "password",
                "password");
        assertThrows(IncorrectRequestException.class, () -> userController.registerNewUser(futureBirthDateDto));

        NewUserDto wrongBirthDateDto = new NewUserDto(
                "AnotherUserName",
                "AnotherUser@mail.com",
                "01-01-2000",
                "password",
                "password");
        assertThrows(IncorrectRequestException.class, () -> userController.registerNewUser(wrongBirthDateDto));
    }

    @Test
    void shouldUpdateUser() {

        UserFullDto firstUserDto = userController.registerNewUser(properNewUserDto);
        Long user1Id = firstUserDto.getId();

        NewUserDto secondNewUserDto = new NewUserDto(
                "anotherProperUserName",
                "anotherProperUser@mail.com",
                birthDateStr,
                "password",
                "password");

        UserFullDto secondUserDto = userController.registerNewUser(secondNewUserDto);
        Long user2Id = secondUserDto.getId();

        // updating name for user 1
        String newName = "newName";
        UserUpdateDto updatedNameNewDto = new UserUpdateDto(newName, null, null);
        UserFullDto updatedNameFullDto = userController.update(user1Id, updatedNameNewDto);

        assertEquals(newName, updatedNameFullDto.getName());
        assertEquals(properNewUserDto.getEmail(), updatedNameFullDto.getEmail());
        assertEquals(properNewUserDto.getBirthDate(), updatedNameFullDto.getBirthDate());

        // error to update name for user 2 to same as user 1
        UserUpdateDto sameNameDto = new UserUpdateDto(newName, null, null);
        assertThrows(ConflictOnRequestException.class, () -> userController.update(user2Id, sameNameDto));

        // updating email for user 1
        String newEmail = "newEmail@mail.com";
        UserUpdateDto updatedEmailNewDto = new UserUpdateDto(null, newEmail, null);
        UserFullDto updatedEmailDto = userController.update(user1Id, updatedEmailNewDto);

        assertEquals(properNewUserDto.getName(), firstUserDto.getName());
        assertEquals(newEmail, updatedEmailDto.getEmail());
        assertEquals(properNewUserDto.getBirthDate(), firstUserDto.getBirthDate());

        // error to update email for user 2 to same as user 1
        UserUpdateDto sameEmailDto = new UserUpdateDto(null, newEmail, null);
        assertThrows(ConflictOnRequestException.class, () -> userController.update(user2Id, sameEmailDto));
    }

    @Test
    void shouldGetUserById() {

        UserFullDto dtoToCheck = userController.registerNewUser(properNewUserDto);

        Long id = dtoToCheck.getId();

        assertEquals(dtoToCheck, userController.getById(id));
    }

    @Test
    void shouldRemoveUserById() {

        UserFullDto dtoToCheck = userController.registerNewUser(properNewUserDto);

        Long id = dtoToCheck.getId();

        assertEquals(dtoToCheck, userController.getById(id));

        userController.removeById(id);

        assertThrows(NotFoundException.class, () -> userController.getById(id));
    }
}