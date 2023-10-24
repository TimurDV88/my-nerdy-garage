package com.mynerdygarage.int_test;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.user.controller.UserController;
import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.vehicle.dto.NewVehicleDto;
import com.mynerdygarage.vehicle.model.FuelType;
import com.mynerdygarage.vehicle.service.VehicleService;
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
class UserAndVehicleServiceTest {

    private final UserController userController;

    private final VehicleService vehicleService;

    private static NewUserDto properNewUserDto;
    private static String birthDateStr;
    private static String releaseDateStr;

    @BeforeEach
    void setUp() {

        birthDateStr = "01.01.1991";
        properNewUserDto = new NewUserDto(
                "ProperUserName",
                "properUser@mail.com",
                birthDateStr);

        releaseDateStr = "01.01.1920";
        NewVehicleDto properNewVehicleDto = new NewVehicleDto(
                1L,
                "Ford",
                "model_T",
                null,
                "Black",
                releaseDateStr,
                1.5,
                FuelType.PETROL,
                20,
                "Classic ford model T"
        );
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
        assertEquals(properNewUserDto.getEmail(), dtoToCheck.getEmail());
        assertEquals(properNewUserDto.getBirthDate(), dtoToCheck.getBirthDate());

        UserFullDto sameNameDto = new UserFullDto(null, newName, null, null, null);
        assertThrows(ConflictOnRequestException.class, () -> userController.update(id, sameNameDto));
    }
}