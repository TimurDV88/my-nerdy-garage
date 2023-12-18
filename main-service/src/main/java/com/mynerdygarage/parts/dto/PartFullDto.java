package com.mynerdygarage.parts.dto;

import com.mynerdygarage.category.dto.CategoryShortDto;
import com.mynerdygarage.user.dto.UserShortDto;
import com.mynerdygarage.vehicle.dto.VehicleShortDto;
import lombok.Data;

@Data
public class PartFullDto {

    private final Long id;
    private final UserShortDto userShortDto;

    private final VehicleShortDto vehicleShortDto;

    private final CategoryShortDto categoryShortDto;

    private final String partNumber;

    private final String name;

    private final String description;

    private final Boolean isReusable;

    private final String status;

    private final String orderDate;

    private final String deliveryDate;
}
