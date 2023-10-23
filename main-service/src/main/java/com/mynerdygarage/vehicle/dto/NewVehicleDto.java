package com.mynerdygarage.vehicle.dto;

import com.mynerdygarage.vehicle.model.FuelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewVehicleDto {

    @NotNull
    @NotBlank
    private final Long ownerId;

    @NotNull
    @NotBlank
    @Size(min = 2, message = "size must be between 2 and 20")
    @Size(max = 20, message = "size must be between 2 and 20")
    private final String producer;

    @NotNull
    @NotBlank
    @Size(min = 2, message = "size must be between 2 and 20")
    @Size(max = 20, message = "size must be between 2 and 20")
    private final String model;

    @Size(min = 2, message = "size must be between 2 and 20")
    @Size(max = 20, message = "size must be between 2 and 20")
    private final String name;

    @Size(min = 2, message = "size must be between 2 and 20")
    @Size(max = 20, message = "size must be between 2 and 20")
    private final String color;

    @Size(min = 12, message = "size must be between 12 and 13")
    @Size(max = 13, message = "size must be between 12 and 13")
    private final String releaseDate;

    private final Double engineVolume;

    private final FuelType fuelType;

    private final Integer power;

    @Size(min = 2, message = "size must be between 2 and 250")
    @Size(max = 20, message = "size must be between 2 and 250")
    private final String description;
}
