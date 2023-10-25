package com.mynerdygarage.vehicle.dto;

import com.mynerdygarage.vehicle.model.FuelType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewVehicleDto {

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

    @Size(min = 2, message = "size must be between 2 and 20")
    @Size(max = 20, message = "size must be between 2 and 20")
    private final String regNumber;

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
