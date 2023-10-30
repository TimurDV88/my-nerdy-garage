package com.mynerdygarage.vehicle.service.util;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.dto.VehicleFullDto;
import com.mynerdygarage.vehicle.repository.VehicleRepository;

import java.time.LocalDate;

public class VehicleChecker {

    public static void check(VehicleRepository vehicleRepository, Long ownerId, VehicleFullDto vehicleDto) {

        if (vehicleRepository.existsByOwnerIdAndNameIgnoreCase(ownerId, vehicleDto.getName())) {
            throw new ConflictOnRequestException("- Vehicle with this Name already exists in garage of this user");
        }

        LocalDate releaseDate = CustomFormatter.stringToDate(vehicleDto.getReleaseDate());

        if (releaseDate != null && releaseDate.isAfter(LocalDate.now())) {
            throw new IncorrectRequestException("- Release date must be before current time");
        }
    }
}
