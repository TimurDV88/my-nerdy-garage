package com.mynerdygarage.int_test;

import com.mynerdygarage.category.controller.CategoryController;
import com.mynerdygarage.category.dto.CategoryFullDto;
import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
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
import com.mynerdygarage.work.dto.WorkShortDto;
import com.mynerdygarage.work.dto.WorkUpdateDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test-h2")
@Transactional
@SpringBootTest
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

        List<CategoryFullDto> categoryList = categoryController.getDefaultCategories(userFullDto.getId());

        category1Id = categoryList.get(0).getId();
        category2Id = categoryList.get(1).getId();
        category3Id = categoryList.get(2).getId();
        category4Id = categoryList.get(3).getId();

        newWorkDto1 = new NewWorkDto("work1", "descr1", vehicleFullDto1.getId(), category1Id,
                "PLANNED", null, null);
        newWorkDto2 = new NewWorkDto("work2", "descr2", vehicleFullDto1.getId(), category2Id,
                null, "01.01.2020", "05.01.2020");
        newWorkDto3 = new NewWorkDto("work3", "descr3", vehicleFullDto2.getId(), category3Id,
                "PLANNED", "01.01.2025", "05.01.2025");
        newWorkDto4 = new NewWorkDto("work4", "descr4", vehicleFullDto2.getId(), category4Id,
                null, "01.01.2021", "05.01.2021");
    }

    @Test
    void shouldAddWork() {

        WorkFullDto workToCheck = workController.addWork(userFullDto.getId(), newWorkDto1);

        assertEquals(newWorkDto1.getTitle(), workToCheck.getTitle());
        assertEquals(newWorkDto1.getDescription(), workToCheck.getDescription());
        assertEquals(newWorkDto1.getVehicleId(), workToCheck.getVehicle().getId());
        assertEquals(newWorkDto1.getCategoryId(), workToCheck.getCategory().getId());
        assertEquals(newWorkDto1.getStatus(), workToCheck.getStatus());
        assertEquals(newWorkDto1.getStartDate(), workToCheck.getStartDate());
        assertEquals(newWorkDto1.getEndDate(), workToCheck.getEndDate());

        //check errors
        NewWorkDto startBeforeEndDto = new NewWorkDto("wrongStart", null, vehicleFullDto1.getId(),
                category1Id, null, "01.01.2022", "01.01.2021");
        assertThrows(ConflictOnRequestException.class,
                () -> workController.addWork(userFullDto.getId(), startBeforeEndDto));

        NewWorkDto startBeforeNowAndIsPlannedDto =
                new NewWorkDto("wrongStart", null, vehicleFullDto1.getId(),
                        category1Id, "PLANNED", "01.01.2022", "01.01.2025");
        assertThrows(ConflictOnRequestException.class,
                () -> workController.addWork(userFullDto.getId(), startBeforeNowAndIsPlannedDto));

        NewWorkDto sameTitleAndStartDate =
                new NewWorkDto("work1", null, vehicleFullDto1.getId(), category1Id,
                        "PLANNED", null, null);
        assertThrows(ConflictOnRequestException.class,
                () -> workController.addWork(userFullDto.getId(), sameTitleAndStartDate));
    }

    @Test
    void shouldUpdateWork() {

        WorkFullDto workToUpdate = workController.addWork(userFullDto.getId(), newWorkDto1);

        WorkUpdateDto workUpdateDto;
        WorkFullDto workToCheckDto;
        Long userId = userFullDto.getId();
        Long workId = workToUpdate.getId();

        //update title
        assertEquals(newWorkDto1.getTitle(), workToUpdate.getTitle());
        workUpdateDto = new WorkUpdateDto(
                "newTitle", null, null, null, null, null);
        workToCheckDto = workController.update(userId, workId, workUpdateDto);
        assertEquals(workUpdateDto.getTitle(), workToCheckDto.getTitle());

        //update category and isPlanned and start and end
        assertEquals(category1Id, workToCheckDto.getCategory().getId());
        assertEquals("PLANNED", workToCheckDto.getStatus());
        assertNull(workToCheckDto.getStartDate());
        assertNull(workToCheckDto.getEndDate());

        Long newCategoryId = category2Id;
        String startDateString = "01.01.2020";
        String endDateString = "02.01.2020";

        workUpdateDto = new WorkUpdateDto(
                null, null, newCategoryId, "DONE", startDateString, endDateString);
        workToCheckDto = workController.update(userId, workId, workUpdateDto);
        assertEquals(newCategoryId, workToCheckDto.getCategory().getId());
        assertNotEquals("PLANNED", workToCheckDto.getStatus());
        assertEquals(startDateString, workToCheckDto.getStartDate());
        assertEquals(endDateString, workToCheckDto.getEndDate());

        //check errors
        // start after end
        WorkUpdateDto startAfterEndDto = new WorkUpdateDto(
                null, null, null, null, "01.01.2021", null);
        assertThrows(ConflictOnRequestException.class,
                () -> workController.update(userId, workId, startAfterEndDto));

        // start before now with isPlanned = true
        WorkUpdateDto startForIsPlanned = new WorkUpdateDto(
                null, null, null, "PLANNED", null, null);
        assertThrows(ConflictOnRequestException.class,
                () -> workController.update(userId, workId, startForIsPlanned));

        // same Title and StartDate as another work
        NewWorkDto anotherWorkNewDto = new NewWorkDto("anotherTitle", null, vehicleFullDto1.getId(),
                category1Id, null, startDateString, endDateString);
        WorkFullDto anotherWorkFullDto = workController.addWork(userId, anotherWorkNewDto);
        assertEquals(anotherWorkNewDto.getTitle(), anotherWorkFullDto.getTitle());
        assertEquals(anotherWorkNewDto.getStartDate(), anotherWorkFullDto.getStartDate());

        WorkUpdateDto sameTitleAndStartDto = new WorkUpdateDto(
                anotherWorkFullDto.getTitle(), null, null, "PLANNED", startDateString, null);
        assertThrows(ConflictOnRequestException.class,
                () -> workController.update(userId, workId, sameTitleAndStartDto));
    }

    @Test
    void shouldGetById() {

        Long userId = userFullDto.getId();

        WorkFullDto addedWork = workController.addWork(userId, newWorkDto1);

        Long workId = addedWork.getId();

        WorkFullDto workToCheck = workController.getById(userId, workId);

        assertEquals(addedWork, workToCheck);

        // check wrong id
        assertThrows(NotFoundException.class,
                () -> workController.getById(userId, 999L));
    }

    @Test
    void shouldGetByVehicleId() {

        Long userId = userFullDto.getId();

        WorkFullDto addedWork1 = workController.addWork(userId, newWorkDto1);
        Long work1Id = addedWork1.getId();

        WorkFullDto addedWork2 = workController.addWork(userId, newWorkDto2);
        Long work2Id = addedWork2.getId();

        List<WorkShortDto> listOfWorks =
                workController.getByVehicleId(userId, vehicleFullDto1.getId(), "id", 0, 10);

        assertEquals(work1Id, listOfWorks.get(0).getId());
        assertEquals(addedWork1.getTitle(), listOfWorks.get(0).getTitle());
        assertEquals(work2Id, listOfWorks.get(1).getId());
        assertEquals(addedWork2.getTitle(), listOfWorks.get(1).getTitle());

        WorkFullDto addedWork3 = workController.addWork(userId, newWorkDto3);
        Long work3Id = addedWork3.getId();

        WorkFullDto addedWork4 = workController.addWork(userId, newWorkDto4);
        Long work4Id = addedWork4.getId();

        listOfWorks =
                workController.getByVehicleId(userId, vehicleFullDto2.getId(), "start_Date", 0, 10);

        assertEquals(work4Id, listOfWorks.get(0).getId());
        assertEquals(addedWork4.getTitle(), listOfWorks.get(0).getTitle());
        assertEquals(work3Id, listOfWorks.get(1).getId());
        assertEquals(addedWork3.getTitle(), listOfWorks.get(1).getTitle());

        // check wrong vehicle id
        assertThrows(NotFoundException.class,
                () -> workController.getByVehicleId(userId, 999L, "id", 0, 10));
    }

    @Test
    void shouldGetByParameters() {

        Long userId = userFullDto.getId();
        Long vehicleId1 = vehicleFullDto1.getId();
        List<WorkFullDto> workList;

        WorkFullDto addedWork1 = workController.addWork(userId, newWorkDto1);
        Long work1Id = addedWork1.getId();

        WorkFullDto addedWork2 = workController.addWork(userId, newWorkDto2);
        Long work2Id = addedWork2.getId();

        WorkFullDto addedWork3 = workController.addWork(userId, newWorkDto3);
        Long work3Id = addedWork3.getId();

        WorkFullDto addedWork4 = workController.addWork(userId, newWorkDto4);
        Long work4Id = addedWork4.getId();

        // by text
        workList = workController.getByParams(userId, "work", null, null,
                null, null, null, "id", 0, 10);
        assertEquals(4, workList.size());
        assertEquals(work1Id, workList.get(0).getId());
        assertEquals(work2Id, workList.get(1).getId());
        assertEquals(work3Id, workList.get(2).getId());
        assertEquals(work4Id, workList.get(3).getId());

        // by vehicle_id with fake id=999L
        workList = workController.getByParams(userId, null, new Long[]{vehicleId1, 999L}, null,
                null, null, null, "id", 0, 10);

        assertEquals(2, workList.size());
        assertEquals(work1Id, workList.get(0).getId());
        assertEquals(work2Id, workList.get(1).getId());

        // by category_id with fake id=999L
        workList =
                workController.getByParams(userId, null, null, new Long[]{category4Id, category3Id, 999L},
                        null, null, null, "id", 0, 10);
        assertEquals(2, workList.size());
        assertEquals(work3Id, workList.get(0).getId());
        assertEquals(work4Id, workList.get(1).getId());

        // by is_planned
        workList = workController.getByParams(userId, null, null, null,
                "PLANNED", null, null, "id", 0, 10);
        assertEquals(2, workList.size());
        assertEquals(work1Id, workList.get(0).getId());
        assertEquals(work3Id, workList.get(1).getId());

        // by start
        workList = workController.getByParams(userId, null, null, null,
                null, "01.01.2025", null, "id", 0, 10);
        assertEquals(1, workList.size());
        assertEquals(work3Id, workList.get(0).getId());

        // by end
        workList = workController.getByParams(userId, null, null, null,
                null, null, "05.01.2021", "id", 0, 10);
        assertEquals(2, workList.size());
        assertEquals(work2Id, workList.get(0).getId());
        assertEquals(work4Id, workList.get(1).getId());

        // test sort by isPlanned
        workList = workController.getByParams(userId, null, null, null,
                null, null, null, "status", 0, 10);
        assertEquals(4, workList.size());
        assertEquals(work2Id, workList.get(0).getId());
        assertEquals(work1Id, workList.get(1).getId());
        assertEquals(work4Id, workList.get(2).getId());
        assertEquals(work3Id, workList.get(3).getId());

        // check error when start after end
        assertThrows(IncorrectRequestException.class, () ->
                workController.getByParams(userId, null, null, null,
                        null, "02.01.2021", "01.01.2021", "id", 0, 10));
    }

    @Test
    void shouldRemoveById() {

        Long userId = userFullDto.getId();
        WorkFullDto addedWork1 = workController.addWork(userId, newWorkDto1);
        Long work1Id = addedWork1.getId();

        assertEquals(addedWork1, workController.getById(userId, work1Id));

        workController.removeById(userId, work1Id);

        assertThrows(NotFoundException.class,
                () -> workController.getById(userId, work1Id));

        // check wrong id
        assertThrows(NotFoundException.class,
                () -> workController.removeById(userId, 999L));
    }
}
