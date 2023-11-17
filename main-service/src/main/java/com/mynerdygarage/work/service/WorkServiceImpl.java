package com.mynerdygarage.work.service;

import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.category.repository.CategoryRepository;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.vehicle.model.Vehicle;
import com.mynerdygarage.vehicle.repository.VehicleRepository;
import com.mynerdygarage.work.dto.NewWorkDto;
import com.mynerdygarage.work.dto.WorkFullDto;
import com.mynerdygarage.work.dto.WorkMapper;
import com.mynerdygarage.work.dto.WorkShortDto;
import com.mynerdygarage.work.model.Work;
import com.mynerdygarage.work.repository.WorkRepository;
import com.mynerdygarage.work.service.util.WorkChecker;
import com.mynerdygarage.work.service.util.WorkCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkServiceImpl implements WorkService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final CategoryRepository categoryRepository;
    private final WorkRepository workRepository;

    @Override
    @Transactional
    public WorkFullDto addWork(Long userId, NewWorkDto newWorkDto) {

        log.info("-- Saving work by user with Id={}: {}", userId, newWorkDto);

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- UserId not found: " + userId));

        Vehicle vehicle = vehicleRepository.findById(newWorkDto.getVehicleId()).orElseThrow(() ->
                new NotFoundException("- VehicleId not found: " + newWorkDto.getVehicleId()));

        Category category = categoryRepository.findById(newWorkDto.getCategoryId()).orElseThrow(() ->
                new NotFoundException("- CategoryId not found: " + newWorkDto.getCategoryId()));

        Work work = WorkCreator.create(user, vehicle, category, newWorkDto);

        WorkChecker.check(workRepository, WorkMapper.workToFullDto(work));

        WorkFullDto fullDtoToReturn = WorkMapper.workToFullDto(workRepository.save(work));

        log.info("-- Work has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public WorkFullDto update(Long userId, Long workId, WorkFullDto inputFullDto) {

        log.info("-- Updating work by workId={}: {}", workId, inputFullDto);

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("- userId not found: " + userId);
        }
/*
        WorkChecker.check(workRepository, inputFullDto);

        Vehicle vehicleToUpdate = vehicleRepository.findById(vehicleId).orElseThrow(() ->
                new NotFoundException("- VehicleId not found: " + vehicleId));

        VehicleUpdater.update(vehicleToUpdate, inputVehicleDto);

        VehicleFullDto fullDtoToReturn = VehicleMapper.vehicleToFullDto(vehicleRepository.save(vehicleToUpdate));

        log.info("-- Vehicle has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;*/
        return null;
    }

    @Override
    public WorkFullDto getById(Long userId, Long workId) {
        return null;
    }

    @Override
    public List<WorkShortDto> getByVehicleId(Long userId, Long vehicleId, int from, int size) {
        return null;
    }

    @Override
    public List<WorkFullDto> getByParams(Long userId, Long[] vehicleIds, Boolean isPlanned, LocalDate start, LocalDate end, String sort, int from, int size) {
        return null;
    }

    @Override
    public void removeById(Long userId, Long workId) {

    }
}
