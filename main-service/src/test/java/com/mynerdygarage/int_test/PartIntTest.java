package com.mynerdygarage.int_test;

import com.mynerdygarage.category.controller.CategoryController;
import com.mynerdygarage.category.dto.CategoryFullDto;
import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.parts.controller.PartController;
import com.mynerdygarage.parts.dto.NewPartDto;
import com.mynerdygarage.parts.dto.PartFullDto;
import com.mynerdygarage.parts.dto.PartUpdateDto;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test-h2")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PartIntTest {

    private final UserController userController;
    private final VehicleController vehicleController;
    private final CategoryController categoryController;
    private final WorkController workController;
    private final PartController partController;

    private VehicleFullDto vehicleFullDto1;
    private Long vehicle1Id;
    private VehicleFullDto vehicleFullDto2;
    private Long vehicle2Id;
    private UserFullDto userFullDto;
    private Long userId;
    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    private Long category4Id;
    private NewWorkDto newWorkDto1;
    private NewWorkDto newWorkDto2;
    private NewPartDto newPartDto1;
    private NewPartDto newPartDto2;
    private NewPartDto newPartDto3;
    private NewPartDto newPartDto4;

    @BeforeEach
    void setUp() {

        String birthDateStr = "01.01.1991";
        NewUserDto properNewUserDto = new NewUserDto(
                "ProperUserName",
                "properUser@mail.com",
                birthDateStr);
        userFullDto = userController.add(properNewUserDto);
        userId = userFullDto.getId();

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
        vehicle1Id = vehicleFullDto1.getId();

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
        vehicle2Id = vehicleFullDto2.getId();

        List<CategoryFullDto> categoryList = categoryController.getDefaultCategories(userFullDto.getId());

        category1Id = categoryList.get(0).getId();
        category2Id = categoryList.get(1).getId();
        category3Id = categoryList.get(2).getId();
        category4Id = categoryList.get(3).getId();

        newWorkDto1 = new NewWorkDto("work1", "descr1", vehicleFullDto1.getId(), category1Id,
                "PLANNED", null, null);
        newWorkDto2 = new NewWorkDto("work2", "descr2", vehicleFullDto1.getId(), category2Id,
                null, "01.01.2020", "05.01.2020");

        newPartDto1 = new NewPartDto(vehicle1Id, category1Id, "partNumber1", "partName1",
                "descr1", true, null, "01.01.2020", "01.02.2020");
        newPartDto2 = new NewPartDto(vehicle2Id, category2Id, "partNumber2", "partName2",
                "descr2", false, null, "01.01.2021", "01.02.2021");
        newPartDto3 = new NewPartDto(vehicle1Id, category3Id, "partNumber3", "partName3",
                "descr3", true, null, "01.01.2020", "01.02.2020");
        newPartDto4 = new NewPartDto(vehicle2Id, category4Id, "partNumber4", "partName4",
                "descr4", false, null, "01.01.2021", "01.02.2021");
    }

    @Test
    void shouldAddPart() {

        PartFullDto partDtoToCheck = partController.addPart(userId, newPartDto1);

        assertEquals(newPartDto1.getVehicleId(), partDtoToCheck.getVehicleShortDto().getId());
        assertEquals(newPartDto1.getCategoryId(), partDtoToCheck.getCategoryShortDto().getId());
        assertEquals(newPartDto1.getPartNumber(), partDtoToCheck.getPartNumber());
        assertEquals(newPartDto1.getName(), partDtoToCheck.getName());
        assertEquals(newPartDto1.getDescription(), partDtoToCheck.getDescription());
        assertEquals(newPartDto1.getIsReusable(), partDtoToCheck.getIsReusable());
        assertEquals(newPartDto1.getStatus(), partDtoToCheck.getStatus());
        assertEquals(newPartDto1.getOrderDate(), partDtoToCheck.getOrderDate());
        assertEquals(newPartDto1.getDeliveryDate(), partDtoToCheck.getDeliveryDate());

        //check errors
        assertThrows(ConflictOnRequestException.class,
                () -> partController.addPart(userId, newPartDto1));

        NewPartDto wrongStatusDto = new NewPartDto(vehicle1Id, category1Id,
                "test",
                "test",
                "test", true,
                "wrongStatus",
                "01.03.2020", "01.02.2020");
        assertThrows(ConflictOnRequestException.class,
                () -> partController.addPart(userId, wrongStatusDto));

        NewPartDto deliveryBeforeOrderDto = new NewPartDto(vehicle1Id, category1Id,
                "test",
                "test",
                "test", true, null,
                "01.03.2020", "01.02.2020");
        assertThrows(ConflictOnRequestException.class,
                () -> partController.addPart(userId, deliveryBeforeOrderDto));

        NewPartDto orderBeforeNowAndStatusIsPlannedDto = new NewPartDto(vehicle1Id, category1Id,
                "test",
                "test",
                "test", true,
                "PLANNED",
                "01.03.2020", "01.02.2020");
        assertThrows(ConflictOnRequestException.class,
                () -> partController.addPart(userId, orderBeforeNowAndStatusIsPlannedDto));

        NewPartDto deliveryAfterNowAndStatusIsNotCancelledOrOnDeliveryDto = new NewPartDto(vehicle1Id, category1Id,
                "test",
                "test",
                "test", true,
                "INSTALLED",
                "01.03.2020", "01.02.2030");
        assertThrows(ConflictOnRequestException.class,
                () -> partController.addPart(userId, deliveryAfterNowAndStatusIsNotCancelledOrOnDeliveryDto));
    }

    @Test
    void shouldUpdatePart() {

        PartFullDto partFullDto = partController.addPart(userId, newPartDto1);
        Long partId = partFullDto.getId();

        PartUpdateDto partUpdateDto1 = new PartUpdateDto(null, null, null,
                "updatedName",
                null, null, null,
                null, null);

        PartFullDto updatedPartDto1 = partController.update(userId, partId, partUpdateDto1);

        assertEquals(partUpdateDto1.getName(), updatedPartDto1.getName());

        PartUpdateDto partUpdateDto2 = new PartUpdateDto(vehicle2Id, category2Id,
                "updatedNumber", "anotherUpdatedName", "updatedDescr",
                false, null,
                "12.11.2020", "12.12.2020");

        PartFullDto updatedPartDto2 = partController.update(userId, partId, partUpdateDto2);

        assertEquals(partUpdateDto2.getVehicleId(), updatedPartDto2.getVehicleShortDto().getId());
        assertEquals(partUpdateDto2.getCategoryId(), updatedPartDto2.getCategoryShortDto().getId());
        assertEquals(partUpdateDto2.getPartNumber(), updatedPartDto2.getPartNumber());
        assertEquals(partUpdateDto2.getName(), updatedPartDto2.getName());
        assertEquals(partUpdateDto2.getDescription(), updatedPartDto2.getDescription());
        assertEquals(partUpdateDto2.getIsReusable(), updatedPartDto2.getIsReusable());
        assertEquals(partUpdateDto2.getStatus(), updatedPartDto2.getStatus());
        assertEquals(partUpdateDto2.getOrderDate(), updatedPartDto2.getOrderDate());
        assertEquals(partUpdateDto2.getDeliveryDate(), updatedPartDto2.getDeliveryDate());

    }

    @Test
    void shouldGetById() {

        PartFullDto partFullDto = partController.addPart(userId, newPartDto1);
        Long partId = partFullDto.getId();

        assertEquals(partFullDto, partController.getById(userId, partId));
    }

    @Test
    void shouldGetByParams() {

        PartFullDto partFullDto1 = partController.addPart(userId, newPartDto1);
        PartFullDto partFullDto2 = partController.addPart(userId, newPartDto2);
        PartFullDto partFullDto3 = partController.addPart(userId, newPartDto3);
        PartFullDto partFullDto4 = partController.addPart(userId, newPartDto4);

        List<PartFullDto> partDtoList;

        // text = "part"
        partDtoList = partController.getByParams(
                userId,
                "part",
                null, null, null, null, null, null,
                "id", 0, 10
        );
        assertEquals(4, partDtoList.size());

        // text = "nothing"
        partDtoList = partController.getByParams(
                userId,
                "nothing",
                null, null, null, null, null, null,
                "id", 0, 10
        );
        assertEquals(0, partDtoList.size());

        // veh ids = 1L
        partDtoList = partController.getByParams(
                userId,
                null,
                new Long[]{vehicle1Id},
                null, null, null, null, null,
                "id", 0, 10
        );

        assertEquals(2, partDtoList.size());
        assertEquals(partFullDto1, partDtoList.get(0));
        assertEquals(partFullDto3, partDtoList.get(1));

        // cat ids = 2,4
        partDtoList = partController.getByParams(
                userId,
                null, null,
                new Long[]{category2Id, category4Id},
                null, null, null, null,
                "id", 0, 10
        );
        assertEquals(2, partDtoList.size());
        assertEquals(partFullDto2, partDtoList.get(0));
        assertEquals(partFullDto4, partDtoList.get(1));

        // isReusable = true
        partDtoList = partController.getByParams(
                userId,
                null, null, null,
                true,
                null, null, null,
                "id", 0, 10
        );
        assertEquals(2, partDtoList.size());
        assertEquals(partFullDto1, partDtoList.get(0));
        assertEquals(partFullDto3, partDtoList.get(1));

        // status = null
        partDtoList = partController.getByParams(
                userId,
                null, null, null, null,
                null,
                null, null,
                "id", 0, 10
        );
        assertEquals(4, partDtoList.size());

        // start = 2021
        partDtoList = partController.getByParams(
                userId,
                null, null, null, null, null,
                "01.01.2021",
                null,
                "id", 0, 10
        );
        assertEquals(2, partDtoList.size());
        assertEquals(partFullDto2, partDtoList.get(0));
        assertEquals(partFullDto4, partDtoList.get(1));

        // end = 2021
        partDtoList = partController.getByParams(
                userId,
                null, null, null, null, null, null,
                "12.12.2020",
                "id", 0, 10
        );
        assertEquals(2, partDtoList.size());
        assertEquals(partFullDto1, partDtoList.get(0));
        assertEquals(partFullDto3, partDtoList.get(1));
    }

    @Test
    void shouldRemoveById() {

        PartFullDto partFullDto = partController.addPart(userId, newPartDto1);
        Long partId = partFullDto.getId();

        assertEquals(partFullDto, partController.getById(userId, partId));

        partController.removeById(userId, partId);

        assertThrows(NotFoundException.class,
                () -> partController.getById(userId, partId));
    }

    /*
        WorkPart service
     */
    @Test
    void shouldAddPartToWorkAndGetByWorkId() {

        PartFullDto partFullDto1 = partController.addPart(userId, newPartDto1);
        Long part1Id = partFullDto1.getId();

        WorkFullDto workFullDto1 = workController.addWork(userId, newWorkDto1);
        Long work1Id = workFullDto1.getId();

        partController.addPartToWork(userId, part1Id, work1Id);

        assertEquals(partFullDto1.getPartNumber(),
                partController.getByWorkId(userId, work1Id, 0, 10).get(0).getPartNumber());
    }

    @Test
    void shouldGetPartListByWorkId() {

        PartFullDto partFullDto1 = partController.addPart(userId, newPartDto1);
        Long part1Id = partFullDto1.getId();
        PartFullDto partFullDto2 = partController.addPart(userId, newPartDto2);
        Long part2Id = partFullDto2.getId();
        PartFullDto partFullDto3 = partController.addPart(userId, newPartDto3);
        Long part3Id = partFullDto3.getId();
        PartFullDto partFullDto4 = partController.addPart(userId, newPartDto4);
        Long part4Id = partFullDto4.getId();

        WorkFullDto workFullDto1 = workController.addWork(userId, newWorkDto1);
        Long work1Id = workFullDto1.getId();
        WorkFullDto workFullDto2 = workController.addWork(userId, newWorkDto2);
        Long work2Id = workFullDto2.getId();

        partController.addPartToWork(userId, part1Id, work1Id);
        partController.addPartToWork(userId, part2Id, work2Id);
        partController.addPartToWork(userId, part3Id, work1Id);
        partController.addPartToWork(userId, part4Id, work2Id);

        assertEquals(partFullDto1.getPartNumber(),
                partController.getByWorkId(userId, work1Id, 0, 10).get(0).getPartNumber());
        assertEquals(partFullDto2.getPartNumber(),
                partController.getByWorkId(userId, work2Id, 0, 10).get(0).getPartNumber());
        assertEquals(partFullDto3.getPartNumber(),
                partController.getByWorkId(userId, work1Id, 0, 10).get(1).getPartNumber());
        assertEquals(partFullDto4.getPartNumber(),
                partController.getByWorkId(userId, work2Id, 0, 10).get(1).getPartNumber());
    }

    @Test
    void shouldRemovePartFromWork() {

        PartFullDto partFullDto1 = partController.addPart(userId, newPartDto1);
        Long part1Id = partFullDto1.getId();

        WorkFullDto workFullDto1 = workController.addWork(userId, newWorkDto1);
        Long work1Id = workFullDto1.getId();

        partController.addPartToWork(userId, part1Id, work1Id);

        assertEquals(1,
                partController.getByWorkId(userId, work1Id, 0, 10).size());

        partController.removePartFromWork(userId, part1Id, work1Id);

        assertEquals(0,
                partController.getByWorkId(userId, work1Id, 0, 10).size());
    }
}
