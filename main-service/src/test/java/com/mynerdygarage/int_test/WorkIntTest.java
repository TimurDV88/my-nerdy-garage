package com.mynerdygarage.int_test;

import com.mynerdygarage.category.controller.CategoryController;
import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.user.controller.UserController;
import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.vehicle.controller.VehicleController;
import com.mynerdygarage.vehicle.dto.NewVehicleDto;
import com.mynerdygarage.vehicle.dto.VehicleFullDto;
import com.mynerdygarage.vehicle.model.FuelType;
import com.mynerdygarage.work.controller.WorkController;
import com.mynerdygarage.work.dto.NewWorkDto;
import com.mynerdygarage.work.dto.WorkFullDto;
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
public class WorkIntTest {

    private final UserController userController;
    private final VehicleController vehicleController;
    private final CategoryController categoryController;
    private final WorkController workController;

    private VehicleFullDto vehicleFullDto1;
    private VehicleFullDto vehicleFullDto2;
    private UserFullDto userFullDto;
    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    private Long category4Id;
    private NewWorkDto newWorkDto1;
    private NewWorkDto newWorkDto2;
    private NewWorkDto newWorkDto3;
    private NewWorkDto newWorkDto4;

    @BeforeEach
    void setUp() {

        String birthDateStr = "01.01.1991";
        NewUserDto properNewUserDto = new NewUserDto(
                "ProperUserName",
                "properUser@mail.com",
                birthDateStr);
        userFullDto = userController.add(properNewUserDto);

        String releaseDateStr = "01.01.1920";
        NewVehicleDto properNewVehicleDto = new NewVehicleDto(
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
        vehicleFullDto1 = vehicleController.addVehicle(userFullDto.getId(), properNewVehicleDto);

        NewVehicleDto anotherNewVehicleDto = new NewVehicleDto(
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
        vehicleFullDto2 = vehicleController.addVehicle(userFullDto.getId(), anotherNewVehicleDto);

        category1Id = categoryController.getDefaultCategories(userFullDto.getId()).get(0).getId();
        category2Id = categoryController.getDefaultCategories(userFullDto.getId()).get(1).getId();
        category3Id = categoryController.getDefaultCategories(userFullDto.getId()).get(2).getId();
        category4Id = categoryController.getDefaultCategories(userFullDto.getId()).get(3).getId();

        newWorkDto1 = new NewWorkDto("work1", "descr1", vehicleFullDto1.getId(), category1Id,
                true, null, null);
        newWorkDto2 = new NewWorkDto("work2", "descr2", vehicleFullDto1.getId(), category2Id,
                false, "01.01.2020", "05.01.2020");
        newWorkDto3 = new NewWorkDto("work3", "descr3", vehicleFullDto2.getId(), category3Id,
                true, "01.01.2025", "05.01.2025");
        newWorkDto4 = new NewWorkDto("work4", "descr4", vehicleFullDto2.getId(), category4Id,
                false, "01.01.2021", "05.01.2021");
    }

    @Test
    void shouldAddWork() {

        WorkFullDto workToCheck = workController.addWork(userFullDto.getId(), newWorkDto1);

        assertEquals(newWorkDto1.getTitle(), workToCheck.getTitle());
        assertEquals(newWorkDto1.getDescription(), workToCheck.getDescription());
        assertEquals(newWorkDto1.getVehicleId(), workToCheck.getVehicle().getId());
        assertEquals(newWorkDto1.getCategoryId(), workToCheck.getCategory().getId());
        assertEquals(newWorkDto1.getIsPlanned(), workToCheck.getIsPlanned());
        assertEquals(newWorkDto1.getStartDate(), workToCheck.getStartDate());
        assertEquals(newWorkDto1.getEndDate(), workToCheck.getEndDate());

        //check errors
        NewWorkDto startBeforeEndDto = new NewWorkDto("wrongStart", null, vehicleFullDto1.getId(),
                category1Id, false, "01.01.2022", "01.01.2021");
        assertThrows(ConflictOnRequestException.class,
                () -> workController.addWork(userFullDto.getId(), startBeforeEndDto));

        NewWorkDto startBeforeNowAndIsPlannedDto =
                new NewWorkDto("wrongStart", null, vehicleFullDto1.getId(),
                category1Id, true, "01.01.2022", "01.01.2025");
        assertThrows(ConflictOnRequestException.class,
                () -> workController.addWork(userFullDto.getId(), startBeforeNowAndIsPlannedDto));

        NewWorkDto sameTitleAndStartDate =
                new NewWorkDto("work1", null, vehicleFullDto1.getId(), category1Id,
                        true, null, null);
        assertThrows(ConflictOnRequestException.class,
                () -> workController.addWork(userFullDto.getId(), sameTitleAndStartDate));
    }

    @Test
    void shouldUpdateWork() {

    }

}
