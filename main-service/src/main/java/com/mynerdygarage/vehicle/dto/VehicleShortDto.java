package com.mynerdygarage.vehicle.dto;

import lombok.Data;

@Data
public class VehicleShortDto {

    private final Long id;

    private final Long ownerId;

    private final String name;

    private final String releaseDate;
}
