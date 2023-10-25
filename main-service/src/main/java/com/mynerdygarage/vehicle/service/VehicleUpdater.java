package com.mynerdygarage.vehicle.service;

import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.util.NullChecker;
import com.mynerdygarage.vehicle.dto.VehicleFullDto;
import com.mynerdygarage.vehicle.dto.VehicleMapper;
import com.mynerdygarage.vehicle.model.Vehicle;
import com.mynerdygarage.vehicle.repository.VehicleRepository;

public class VehicleUpdater {

    public static VehicleFullDto update(VehicleRepository vehicleRepository,
                                        Long ownerId, Long vehicleId, VehicleFullDto inputVehicleDto) {

        VehicleChecker.isCorrect(vehicleRepository, ownerId, inputVehicleDto);

        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() ->
                new NotFoundException("- VehicleId not found: " + vehicleId));

        NullChecker.setIfNotNull(vehicle::setProducer, inputVehicleDto.getProducer());
        NullChecker.setIfNotNull(vehicle::setModel, inputVehicleDto.getModel());
        NullChecker.setIfNotNull(vehicle::setName, inputVehicleDto.getName());
        NullChecker.setIfNotNull(vehicle::setReleaseDate,
                CustomFormatter.stringToDate(inputVehicleDto.getReleaseDate()));
        NullChecker.setIfNotNull(vehicle::setEngineVolume, inputVehicleDto.getEngineVolume());
        NullChecker.setIfNotNull(vehicle::setFuelType, String.valueOf(inputVehicleDto.getFuelType()));
        NullChecker.setIfNotNull(vehicle::setPower, inputVehicleDto.getPower());
        NullChecker.setIfNotNull(vehicle::setDescription, inputVehicleDto.getDescription());

        return VehicleMapper.vehicleToFullDto(vehicleRepository.save(vehicle));
    }
}
