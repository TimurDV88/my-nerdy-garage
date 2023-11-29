package com.mynerdygarage.work.dto;

import com.mynerdygarage.category.dto.CategoryShortDto;
import com.mynerdygarage.user.dto.UserShortDto;
import com.mynerdygarage.vehicle.dto.VehicleShortDto;
import lombok.Data;

@Data
public class WorkShortDto {

    private final Long id;

    private final UserShortDto user;

    private final VehicleShortDto vehicle;

    private final CategoryShortDto category;

    private final String title;

    private final String description;
}
