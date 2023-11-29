package com.mynerdygarage.work.dto;

import com.mynerdygarage.category.dto.CategoryMapper;
import com.mynerdygarage.user.dto.UserMapper;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.dto.VehicleMapper;
import com.mynerdygarage.work.model.Work;

import java.util.ArrayList;
import java.util.List;

public class WorkMapper {

    public static WorkFullDto workToFullDto(Work work) {

        return new WorkFullDto(
                work.getId(),
                UserMapper.userToShortDto(work.getUser()),
                VehicleMapper.vehicleToShortDto(work.getVehicle()),
                CategoryMapper.categoryToShortDto(work.getCategory()),
                work.getTitle(),
                work.getDescription(),
                work.getIsPlanned(),
                CustomFormatter.dateToString(work.getStartDate()),
                CustomFormatter.dateToString(work.getEndDate())
        );
    }

    public static List<WorkFullDto> workToFullDto(Iterable<Work> works) {

        List<WorkFullDto> listToReturn = new ArrayList<>();

        for (Work work : works) {
            listToReturn.add(workToFullDto(work));
        }

        return listToReturn;
    }

    public static WorkShortDto workToShortDto(Work work) {

        return new WorkShortDto(
                work.getId(),
                UserMapper.userToShortDto(work.getUser()),
                VehicleMapper.vehicleToShortDto(work.getVehicle()),
                CategoryMapper.categoryToShortDto(work.getCategory()),
                work.getTitle(),
                work.getDescription()
        );
    }

    public static List<WorkShortDto> workToShortDto(Iterable<Work> works) {

        List<WorkShortDto> listToReturn = new ArrayList<>();

        for (Work work : works) {
            listToReturn.add(workToShortDto(work));
        }

        return listToReturn;
    }
}
