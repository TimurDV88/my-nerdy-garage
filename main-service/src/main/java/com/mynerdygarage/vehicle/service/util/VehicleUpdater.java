package com.mynerdygarage.vehicle.service.util;

import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.util.NullChecker;
import com.mynerdygarage.vehicle.dto.VehicleFullDto;
import com.mynerdygarage.vehicle.model.Vehicle;

public class VehicleUpdater {

    public static Vehicle update(Vehicle vehicleToUpdate, VehicleFullDto inputDto) {

        NullChecker.setIfNotNull(vehicleToUpdate::setProducer, inputDto.getProducer());
        NullChecker.setIfNotNull(vehicleToUpdate::setModel, inputDto.getModel());
        NullChecker.setIfNotNull(vehicleToUpdate::setName, inputDto.getName());
        NullChecker.setIfNotNull(vehicleToUpdate::setReleaseDate,
                CustomFormatter.stringToDate(inputDto.getReleaseDate()));
        NullChecker.setIfNotNull(vehicleToUpdate::setEngineVolume, inputDto.getEngineVolume());
        NullChecker.setIfNotNull(vehicleToUpdate::setFuelType, String.valueOf(inputDto.getFuelType()));
        NullChecker.setIfNotNull(vehicleToUpdate::setPower, inputDto.getPower());
        NullChecker.setIfNotNull(vehicleToUpdate::setDescription, inputDto.getDescription());

        return vehicleToUpdate;
    }
}
