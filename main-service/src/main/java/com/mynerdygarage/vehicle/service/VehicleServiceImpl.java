package com.mynerdygarage.vehicle.service;

import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.util.PageRequestCreator;
import com.mynerdygarage.vehicle.dto.*;
import com.mynerdygarage.vehicle.model.Vehicle;
import com.mynerdygarage.vehicle.repository.VehicleRepository;
import com.mynerdygarage.vehicle.service.util.VehicleChecker;
import com.mynerdygarage.vehicle.service.util.VehicleCreator;
import com.mynerdygarage.vehicle.service.util.VehicleUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public VehicleFullDto addVehicle(Long ownerId, NewVehicleDto newVehicleDto) {

        log.info("-- Saving vehicle by user with Id={}: {}", ownerId, newVehicleDto);

        User owner = userRepository.findById(ownerId).orElseThrow(() ->
                new NotFoundException("- OwnerId not found: " + ownerId));

        Vehicle vehicle = VehicleCreator.createFromNewDto(owner, newVehicleDto);

        VehicleChecker.check(vehicleRepository, ownerId, vehicle);

        VehicleFullDto fullDtoToReturn = VehicleMapper.modelToFullDto(vehicleRepository.save(vehicle));

        log.info("-- Vehicle has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public VehicleFullDto update(Long ownerId, Long vehicleId, VehicleUpdateDto inputVehicleDto) {

        log.info("-- Updating vehicle by vehicleId={}: {}", vehicleId, inputVehicleDto);

        Vehicle vehicleToUpdate = vehicleRepository.findById(vehicleId).orElseThrow(() ->
                new NotFoundException("- VehicleId not found: " + vehicleId));

        if (!ownerId.equals(vehicleToUpdate.getOwner().getId())) {
            throw new NotFoundException("- User with Id=" + ownerId + " is not owner of vehicle with id=" + vehicleId);
        }

        Vehicle inputVehicle = VehicleCreator.createFromUpdateDto(inputVehicleDto);

        VehicleChecker.check(vehicleRepository, ownerId, inputVehicle);

        VehicleUpdater.update(vehicleToUpdate, inputVehicle);

        VehicleFullDto fullDtoToReturn = VehicleMapper.modelToFullDto(vehicleRepository.save(vehicleToUpdate));

        log.info("-- Vehicle has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public VehicleFullDto getById(Long ownerId, Long vehicleId) {

        log.info("-- Getting vehicle by vehicleId={}", vehicleId);

        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("- OwnerId not found: " + ownerId);
        }

        VehicleFullDto fullDtoToReturn =
                VehicleMapper.modelToFullDto(vehicleRepository.findById(vehicleId).orElseThrow(() ->
                        new NotFoundException("- VehicleId not found: " + vehicleId)));

        log.info("-- Vehicle returned: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public List<VehicleShortDto> getByOwnerId(Long ownerId, int from, int size) {

        log.info("-- Getting vehicles by ownerId={}", ownerId);

        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("- OwnerId not found: " + ownerId);
        }

        Sort sort = Sort.by("id").ascending();
        PageRequest pageRequest = PageRequestCreator.create(from, size, sort);

        List<VehicleShortDto> listToReturn =
                VehicleMapper.modelToShortDto(vehicleRepository.findByOwnerId(ownerId, pageRequest));

        log.info("-- Vehicle list for owner with id={} returned, size={}", ownerId, listToReturn.size());

        return listToReturn;
    }

    @Override
    @Transactional
    public void removeById(Long ownerId, Long vehicleId) {

        log.info("--- Deleting vehicle by vehicleId={}", vehicleId);

        VehicleFullDto dtoToShowInLog = getById(ownerId, vehicleId);

        vehicleRepository.deleteById(vehicleId);

        log.info("--- Vehicle deleted: {}", dtoToShowInLog);
    }
}
