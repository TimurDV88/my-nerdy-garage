package com.mynerdygarage.vehicle.service;

import com.mynerdygarage.vehicle.dto.NewVehicleDto;
import com.mynerdygarage.vehicle.dto.VehicleFullDto;
import com.mynerdygarage.vehicle.dto.VehicleShortDto;
import com.mynerdygarage.vehicle.dto.VehicleUpdateDto;

import java.util.List;

public interface VehicleService {

    VehicleFullDto addVehicle(Long ownerId, NewVehicleDto newVehicleDto);

    VehicleFullDto update(Long ownerId, Long vehicleId, VehicleUpdateDto vehicleUpdateDto);

    VehicleFullDto getById(Long ownerId, Long vehicleId);

    List<VehicleShortDto> getByOwnerId(Long ownerId, int from, int size);

    void removeById(Long ownerId, Long vehicleId);
}
