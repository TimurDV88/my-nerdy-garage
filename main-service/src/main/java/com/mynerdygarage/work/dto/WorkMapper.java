package com.mynerdygarage.work.dto;

import com.mynerdygarage.category.dto.CategoryMapper;
import com.mynerdygarage.user.dto.UserMapper;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.dto.VehicleMapper;
import com.mynerdygarage.work.model.Work;

import java.util.List;
import java.util.stream.StreamSupport;

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

        return StreamSupport.stream(works.spliterator(), false)
                .map(WorkMapper::modelToFullDto)
                .toList();
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

        return StreamSupport.stream(works.spliterator(), false)
                .map(WorkMapper::modelToShortDto)
                .toList();
    }
}
