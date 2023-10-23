package com.mynerdygarage.vehicle.service;

import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.util.NullChecker;
import com.mynerdygarage.util.PageRequestCreator;
import com.mynerdygarage.vehicle.dto.NewVehicleDto;
import com.mynerdygarage.vehicle.dto.VehicleFullDto;
import com.mynerdygarage.vehicle.dto.VehicleMapper;
import com.mynerdygarage.vehicle.dto.VehicleShortDto;
import com.mynerdygarage.vehicle.model.Vehicle;
import com.mynerdygarage.vehicle.repository.VehicleRepository;
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

        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("- OwnerId not found: " + ownerId);
        }

        Vehicle vehicle = VehicleCreator.create(newVehicleDto);

        if (VehicleChecker.isNotCorrect(vehicleRepository, VehicleMapper.vehicleToFullDto(vehicle))) {
            log.info("-- Vehicle has NOT been saved: {}", newVehicleDto);
            return null;
        }

        VehicleFullDto fullDtoToReturn = VehicleMapper.vehicleToFullDto(vehicleRepository.save(vehicle));

        log.info("-- Vehicle has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public VehicleFullDto update(Long ownerId, Long vehicleId, VehicleFullDto inputVehicleDto) {

        log.info("-- Updating vehicle by vehicleIdId={}: {}", vehicleId, inputVehicleDto);

        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("- OwnerId not found: " + ownerId);
        }

        if (VehicleChecker.isNotCorrect(vehicleRepository, inputVehicleDto)) {
            log.info("-- Vehicle with vehicleId={} has NOT been updated", vehicleId);
            return null;
        }

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

        VehicleFullDto fullDtoToReturn = VehicleMapper.vehicleToFullDto(vehicleRepository.save(vehicle));

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
                VehicleMapper.vehicleToFullDto(vehicleRepository.findById(vehicleId).orElseThrow(() ->
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
        PageRequest pageRequest = PageRequestCreator.getPageRequest(from, size, sort);

        List<VehicleShortDto> listToReturn =
                VehicleMapper.vehicleToShortDto(vehicleRepository.findByOwnerId(ownerId, pageRequest));

        log.info("-- Vehicle list for owner with id={} returned, size={}", ownerId, listToReturn.size());

        return listToReturn;
    }

    @Override
    @Transactional
    public void removeById(Long ownerId, Long vehicleId) {

        log.info("--- Deleting vehicle by vehicleId={}", vehicleId);

        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("- OwnerId not found: " + ownerId);
        }

        Vehicle vehicleToCheck = vehicleRepository.findById(vehicleId).orElseThrow(() ->
                new NotFoundException("- VehicleId not found: " + vehicleId));

        VehicleFullDto dtoToShowInLog = VehicleMapper.vehicleToFullDto(vehicleToCheck);

        userRepository.deleteById(vehicleId);

        log.info("--- Vehicle deleted: {}", dtoToShowInLog);
    }
}
