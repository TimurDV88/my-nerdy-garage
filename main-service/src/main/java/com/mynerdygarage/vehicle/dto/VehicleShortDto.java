package com.mynerdygarage.vehicle.dto;

import com.mynerdygarage.user.dto.UserShortDto;
import lombok.Data;

@Data
public class VehicleShortDto {

    private final Long id;

    private final UserShortDto owner;

    private final String name;

    private final String releaseDate;
}
