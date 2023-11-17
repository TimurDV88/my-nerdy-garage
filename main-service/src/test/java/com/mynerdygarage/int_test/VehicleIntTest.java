package com.mynerdygarage.int_test;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.controller.UserController;
import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.vehicle.controller.VehicleController;
import com.mynerdygarage.vehicle.dto.NewVehicleDto;
import com.mynerdygarage.vehicle.dto.VehicleFullDto;
import com.mynerdygarage.vehicle.model.FuelType;
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
public class VehicleIntTest {

    private final UserController userController;

    private final VehicleController vehicleController;

    private static NewUserDto properNewUserDto;

    private static NewVehicleDto properNewVehicleDto;

    private static NewVehicleDto anotherNewVehicleDto;

    @BeforeEach
    void setUp() {

        String birthDateStr = "01.01.1991";
        properNewUserDto = new NewUserDto(
                "ProperUserName",
                "properUser@mail.com",
                birthDateStr);

        String releaseDateStr = "01.01.1920";
        properNewVehicleDto = new NewVehicleDto(
                "Ford",
                "model_T",
                null,
                "Black",
                "t123tt",
                releaseDateStr,
                1.5,
                FuelType.PETROL,
                20,
                "Classic ford model T"
        );

        anotherNewVehicleDto = new NewVehicleDto(
                "Toyota",
                "corolla",
                null,
                "White",
                "c321cc",
                releaseDateStr,
                2.0,
                FuelType.PETROL,
                110,
                "Another boring car"
        );
    }

    @Test
    void shouldAddVehicle() {

        UserFullDto userFullDto = userController.add(properNewUserDto);

        Long ownerId = userFullDto.getId();

        VehicleFullDto vehicleDtoToCheck = vehicleController.addVehicle(ownerId, properNewVehicleDto);

        String nameToCheck = properNewVehicleDto.getProducer() + " " + properNewVehicleDto.getModel();
        assertEquals(nameToCheck, vehicleDtoToCheck.getName());
        assertEquals(properNewVehicleDto.getReleaseDate(), vehicleDtoToCheck.getReleaseDate());

        NewVehicleDto existedNameDto = new NewVehicleDto(
                "Ford",
                "Model_A",
                nameToCheck,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        assertThrows(ConflictOnRequestException.class, () -> vehicleController.addVehicle(ownerId, existedNameDto));
    }

    @Test
    void shouldUpdateVehicle() {

        UserFullDto userFullDto = userController.add(properNewUserDto);

        Long ownerId = userFullDto.getId();

        VehicleFullDto vehicleDtoToCheck = vehicleController.addVehicle(ownerId, properNewVehicleDto);

        Long vehicleId = vehicleDtoToCheck.getId();

        String newName = "newName";
        VehicleFullDto updatedNameDto = new VehicleFullDto(null, null, null, null,
                newName, null, null, null, null, null, null, null);
        updatedNameDto = vehicleController.update(ownerId, vehicleId, updatedNameDto);

        assertEquals(newName, updatedNameDto.getName());
        assertEquals(properNewVehicleDto.getProducer(), updatedNameDto.getProducer());
        assertEquals(properNewVehicleDto.getModel(), updatedNameDto.getModel());

        VehicleFullDto sameNameDto = new VehicleFullDto(
                null, null, null, null,
                newName, null, null, null,
                null, null, null, null);
        assertThrows(ConflictOnRequestException.class, () -> vehicleController.update(ownerId, vehicleId, sameNameDto));

        String NewNameUpperCase = newName.toUpperCase();
        VehicleFullDto sameNameUpperCaseDto = new VehicleFullDto(
                null, null, null, null,
                NewNameUpperCase, null, null, null,
                null, null, null, null);
        assertThrows(ConflictOnRequestException.class, () ->
                vehicleController.update(ownerId, vehicleId, sameNameUpperCaseDto));
    }

    @Test
    void shouldGetVehicleById() {

        UserFullDto userFullDto = userController.add(properNewUserDto);

        Long ownerId = userFullDto.getId();

        VehicleFullDto vehicleDtoToCheck = vehicleController.addVehicle(ownerId, properNewVehicleDto);

        Long vehicleId = vehicleDtoToCheck.getId();

        assertEquals(vehicleDtoToCheck, vehicleController.getById(ownerId, vehicleId));
    }

    @Test
    void shouldGetVehiclesByOwnerId() {

        UserFullDto userFullDto = userController.add(properNewUserDto);

        Long ownerId = userFullDto.getId();

        VehicleFullDto vehicleDtoToCheckNo1 = vehicleController.addVehicle(ownerId, properNewVehicleDto);

        VehicleFullDto vehicleDtoToCheckNo2 = vehicleController.addVehicle(ownerId, anotherNewVehicleDto);

        assertEquals(vehicleDtoToCheckNo1.getId(),
                vehicleController.getByOwnerId(ownerId, 0, 10).get(0).getId());
        assertEquals(vehicleDtoToCheckNo1.getName(),
                vehicleController.getByOwnerId(ownerId, 0, 10).get(0).getName());

        assertEquals(vehicleDtoToCheckNo2.getId(),
                vehicleController.getByOwnerId(ownerId, 0, 10).get(1).getId());
        assertEquals(vehicleDtoToCheckNo2.getName(),
                vehicleController.getByOwnerId(ownerId, 0, 10).get(1).getName());
    }

    @Test
    void shouldRemoveVehicleById() {

        UserFullDto userFullDto = userController.add(properNewUserDto);

        Long ownerId = userFullDto.getId();

        VehicleFullDto vehicleDtoToCheck = vehicleController.addVehicle(ownerId, properNewVehicleDto);

        Long vehicleId = vehicleDtoToCheck.getId();

        assertEquals(vehicleDtoToCheck, vehicleController.getById(ownerId, vehicleId));

        vehicleController.removeById(ownerId, vehicleId);

        assertThrows(NotFoundException.class, () -> vehicleController.getById(ownerId, vehicleId));
    }
}
