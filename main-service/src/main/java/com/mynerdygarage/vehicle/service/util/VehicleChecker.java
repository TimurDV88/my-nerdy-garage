package com.mynerdygarage.vehicle.service.util;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.vehicle.model.Vehicle;
import com.mynerdygarage.vehicle.repository.VehicleRepository;

import java.time.LocalDate;

public class VehicleChecker {

    public static void check(VehicleRepository vehicleRepository, Long ownerId, Vehicle inputVehicle) {

        if (vehicleRepository.existsByOwnerIdAndNameIgnoreCase(ownerId, inputVehicle.getName())) {
            throw new ConflictOnRequestException("- Vehicle with this Name already exists in garage of this user");
        }

        LocalDate releaseDate = inputVehicle.getReleaseDate();

        if (releaseDate != null && releaseDate.isAfter(LocalDate.now())) {
            throw new IncorrectRequestException("- Release date must be before current time");
        }
    }
}
