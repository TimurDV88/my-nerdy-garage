package com.mynerdygarage.vehicle.dto;

import com.mynerdygarage.vehicle.model.FuelType;
import lombok.Data;

@Data
public class VehicleFullDto {

    private final Long id;

    private final Long ownerId;

    private final String producer;

    private final String model;

    private final String name;

    private final String color;

    private final String releaseDate;

    private final Double engineVolume;

    private final FuelType fuelType;

    private final Integer power;

    private final String description;
}
