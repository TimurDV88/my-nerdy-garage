package com.mynerdygarage.work.dto;

import com.mynerdygarage.category.dto.CategoryMapper;
import com.mynerdygarage.user.dto.UserMapper;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.dto.VehicleMapper;
import com.mynerdygarage.work.model.Work;

import java.util.ArrayList;
import java.util.List;

public class WorkMapper {

    public static WorkFullDto modelToFullDto(Work work) {

        return new WorkFullDto(
                work.getId(),
                UserMapper.modelToShortDto(work.getInitiator()),
                VehicleMapper.modelToShortDto(work.getVehicle()),
                CategoryMapper.modelToShortDto(work.getCategory()),
                work.getTitle(),
                work.getDescription(),
                work.getStatus(),
                CustomFormatter.dateToString(work.getStartDate()),
                CustomFormatter.dateToString(work.getEndDate())
        );
    }

    public static List<WorkFullDto> modelToFullDto(Iterable<Work> works) {

        List<WorkFullDto> listToReturn = new ArrayList<>();

        for (Work work : works) {
            listToReturn.add(modelToFullDto(work));
        }

        return listToReturn;
    }

    public static WorkShortDto modelToShortDto(Work work) {

        return new WorkShortDto(
                work.getId(),
                UserMapper.modelToShortDto(work.getInitiator()),
                VehicleMapper.modelToShortDto(work.getVehicle()),
                CategoryMapper.modelToShortDto(work.getCategory()),
                work.getTitle(),
                work.getDescription()
        );
    }

    public static List<WorkShortDto> modelToShortDto(Iterable<Work> works) {

        List<WorkShortDto> listToReturn = new ArrayList<>();

        for (Work work : works) {
            listToReturn.add(modelToShortDto(work));
        }

        return listToReturn;
    }
}
