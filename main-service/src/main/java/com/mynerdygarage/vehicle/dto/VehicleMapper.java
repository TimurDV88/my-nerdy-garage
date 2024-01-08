package com.mynerdygarage.vehicle.dto;

import com.mynerdygarage.user.dto.UserMapper;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleMapper {

    public static VehicleFullDto modelToFullDto(Vehicle vehicle) {

        return new VehicleFullDto(
                vehicle.getId(),
                UserMapper.modelToShortDto(vehicle.getOwner()),
                vehicle.getProducer(),
                vehicle.getModel(),
                vehicle.getName(),
                vehicle.getColor(),
                vehicle.getRegNumber(),
                CustomFormatter.dateToString(vehicle.getReleaseDate()),
                vehicle.getEngineVolume(),
                vehicle.getFuelType(),
                vehicle.getPower(),
                vehicle.getDescription()
        );
    }

    public static List<VehicleFullDto> modelToFullDto(Iterable<Vehicle> vehicles) {

        List<VehicleFullDto> toReturn = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            toReturn.add(modelToFullDto(vehicle));
        }

        return toReturn;
    }

    public static VehicleShortDto modelToShortDto(Vehicle vehicle) {

        return new VehicleShortDto(
                vehicle.getId(),
                UserMapper.modelToShortDto(vehicle.getOwner()),
                vehicle.getName(),
                CustomFormatter.dateToString(vehicle.getReleaseDate())
        );
    }

    public static List<VehicleShortDto> modelToShortDto(Iterable<Vehicle> vehicles) {

        List<VehicleShortDto> toReturn = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            toReturn.add(modelToShortDto(vehicle));
        }

        return toReturn;
    }
}
