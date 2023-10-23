package com.mynerdygarage.vehicle.dto;

import com.mynerdygarage.vehicle.model.FuelType;
import com.mynerdygarage.vehicle.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleMapper {

    public static VehicleFullDto vehicleToFullDto(Vehicle vehicle) {

        return new VehicleFullDto(
                vehicle.getId(),
                vehicle.getOwnerId(),
                vehicle.getName(),
                vehicle.getProducer(),
                vehicle.getModel(),
                vehicle.getColor(),
                String.valueOf(vehicle.getReleaseDate()),
                vehicle.getEngineVolume(),
                FuelType.valueOf(vehicle.getFuelType()),
                vehicle.getPower(),
                vehicle.getDescription()
        );
    }

    public static List<VehicleFullDto> vehicleToFullDto(Iterable<Vehicle> vehicles) {

        List<VehicleFullDto> toReturn = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            toReturn.add(vehicleToFullDto(vehicle));
        }

        return toReturn;
    }

    public static VehicleShortDto vehicleToShortDto(Vehicle vehicle) {

        return new VehicleShortDto(
                vehicle.getId(),
                vehicle.getOwnerId(),
                vehicle.getName(),
                String.valueOf(vehicle.getReleaseDate())
        );
    }

    public static List<VehicleShortDto> vehicleToShortDto(Iterable<Vehicle> vehicles) {

        List<VehicleShortDto> toReturn = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            toReturn.add(vehicleToShortDto(vehicle));
        }

        return toReturn;
    }
}
