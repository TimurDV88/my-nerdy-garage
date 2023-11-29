package com.mynerdygarage.vehicle.service.util;

import com.mynerdygarage.util.NullChecker;
import com.mynerdygarage.vehicle.model.Vehicle;

public class VehicleUpdater {

    public static Vehicle update(Vehicle vehicleToUpdate, Vehicle inputVehicle) {

        NullChecker.setIfNotNull(vehicleToUpdate::setProducer, inputVehicle.getProducer());
        NullChecker.setIfNotNull(vehicleToUpdate::setModel, inputVehicle.getModel());
        NullChecker.setIfNotNull(vehicleToUpdate::setName, inputVehicle.getName());
        NullChecker.setIfNotNull(vehicleToUpdate::setReleaseDate, inputVehicle.getReleaseDate());
        NullChecker.setIfNotNull(vehicleToUpdate::setEngineVolume, inputVehicle.getEngineVolume());
        NullChecker.setIfNotNull(vehicleToUpdate::setFuelType, String.valueOf(inputVehicle.getFuelType()));
        NullChecker.setIfNotNull(vehicleToUpdate::setPower, inputVehicle.getPower());
        NullChecker.setIfNotNull(vehicleToUpdate::setDescription, inputVehicle.getDescription());

        return vehicleToUpdate;
    }
}
