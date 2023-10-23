package com.mynerdygarage.vehicle.service;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.dto.VehicleFullDto;
import com.mynerdygarage.vehicle.repository.VehicleRepository;

import java.time.LocalDate;

public class VehicleChecker {

    public static boolean isNotCorrect(VehicleRepository vehicleRepository, VehicleFullDto vehicle) {

        if (vehicleRepository.existsByOwnerIdAndName(vehicle.getOwnerId(), vehicle.getName())) {
            throw new ConflictOnRequestException("- Vehicle with this Name already exists in garage of this user");
        }

        LocalDate releaseDate = CustomFormatter.stringToDate(vehicle.getReleaseDate());

        if (releaseDate.isAfter(LocalDate.now())) {
            throw new IncorrectRequestException("- Release date must be before current time");
        }

        return true;
    }
}
