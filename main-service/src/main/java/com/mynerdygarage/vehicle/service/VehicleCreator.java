package com.mynerdygarage.vehicle.service;

import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.dto.NewVehicleDto;
import com.mynerdygarage.vehicle.model.Vehicle;

public class VehicleCreator {

    public static Vehicle create(NewVehicleDto newVehicleDto) {

        Vehicle vehicle = new Vehicle();

        vehicle.setOwnerId(newVehicleDto.getOwnerId());
        vehicle.setProducer(newVehicleDto.getProducer());
        vehicle.setModel(newVehicleDto.getModel());

        String name;
        if (newVehicleDto.getName() == null) {
            name = newVehicleDto.getProducer() + " " + newVehicleDto.getModel();
        } else {
            name = newVehicleDto.getName();
        }
        vehicle.setName(name);

        vehicle.setColor(newVehicleDto.getColor());
        vehicle.setReleaseDate(CustomFormatter.stringToDate(newVehicleDto.getReleaseDate()));
        vehicle.setEngineVolume(newVehicleDto.getEngineVolume());
        vehicle.setFuelType(newVehicleDto.getFuelType().toString());
        vehicle.setPower(newVehicleDto.getPower());
        vehicle.setDescription(newVehicleDto.getDescription());

        return vehicle;
    }
}
