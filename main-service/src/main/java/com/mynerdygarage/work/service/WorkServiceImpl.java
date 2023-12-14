package com.mynerdygarage.work.service;

import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.category.repository.CategoryRepository;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.util.PageRequestCreator;
import com.mynerdygarage.vehicle.model.Vehicle;
import com.mynerdygarage.vehicle.repository.VehicleRepository;
import com.mynerdygarage.work.dto.*;
import com.mynerdygarage.work.model.Work;
import com.mynerdygarage.work.repository.WorkRepository;
import com.mynerdygarage.work.service.util.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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

        Work work = WorkCreator.createFromNewDto(user, vehicle, category, newWorkDto);

        WorkChecker.checkNewWork(workRepository, work);

        WorkFullDto fullDtoToReturn = WorkMapper.workToFullDto(workRepository.save(work));

        log.info("-- Work has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public WorkFullDto update(Long userId, Long workId, WorkUpdateDto inputDto) {

        log.info("-- Updating work by workId={}: {}", workId, inputDto);

        Work workToUpdate = workRepository.findById(workId).orElseThrow(() ->
                new NotFoundException("- WorkId not found: " + workId));

        if (!userId.equals(workToUpdate.getUser().getId())) {
            throw new NotFoundException("- User with Id=" + userId + " is not initiator of work with id=" + workId);
        }

        Category category;
        if (inputDto.getCategoryId() != null) {
            category = categoryRepository.findById(inputDto.getCategoryId()).orElseThrow(() ->
                    new NotFoundException("- CategoryId not found: " + inputDto.getCategoryId()));
        } else {
            category = workToUpdate.getCategory();
        }

        Work inputWork = WorkCreator.createFromUpdateDto(category, inputDto);

        WorkUpdater.update(workToUpdate, inputWork);

        WorkChecker.checkUpdateWork(workRepository, workToUpdate);

        WorkFullDto fullDtoToReturn = WorkMapper.workToFullDto(workRepository.save(workToUpdate));

        log.info("-- Work has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public WorkFullDto getById(Long userId, Long workId) {

        log.info("-- Returning work by workId={}", workId);

        WorkFullDto fullDtoToReturn = WorkMapper.workToFullDto(workRepository.findById(workId).orElseThrow(() ->
                new NotFoundException("- WorkId not found: " + workId)));

        if (!userId.equals(fullDtoToReturn.getUser().getId())) {
            throw new NotFoundException("- User with Id=" + userId + " is not initiator of work with id=" + workId);
        }

        log.info("-- Work returned: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public List<WorkShortDto> getByVehicleId(Long userId, Long vehicleId, String sortBy, int from, int size) {

        log.info("-- Returning works list by vehicleId={}", vehicleId);

        if (!vehicleRepository.existsByOwnerIdAndId(userId, vehicleId)) {
            throw new NotFoundException("- User with id=" + userId + " does not own vehicle with id=" + vehicleId);
        }

        Sort sort = WorkSorter.createSort(sortBy);
        PageRequest pageRequest = PageRequestCreator.create(from, size, sort);

        Iterable<Work> foundWorks = workRepository.findByVehicleId(vehicleId, pageRequest);

        List<WorkShortDto> listToReturn = WorkMapper.workToShortDto(foundWorks);

        log.info("-- Work list for vehicleId={} returned, size={}", vehicleId, listToReturn.size());

        return listToReturn;
    }

    @Override
    public List<WorkFullDto> getByParams(Long userId,
                                         String text,
                                         Long[] vehicleIds,
                                         Long[] categoryIds,
                                         Boolean isPlanned,
                                         String start,
                                         String end,
                                         String sortBy, int from, int size) {

        log.info("-- Returning works list by parameters: text={}, vehicleIds={}, categoryIds={}, isPlanned={}, " +
                        "startFromDate={}, endByDate={}, sort={}",
                text, vehicleIds, categoryIds, isPlanned, start, end, sortBy);

        // checks:

        // text
        if (text != null && (text.isBlank() || text.length() < 2)) {
            text = null;
        }

        // vehicleIds
        if (vehicleIds != null) {

            List<Long> existedVehicleIds = vehicleRepository.findIdsByOwnerId(userId);
            List<Long> properVehicleIds = new ArrayList<>();

            for (Long inputVehicleId : vehicleIds) {
                if (existedVehicleIds.contains(inputVehicleId)) {
                    properVehicleIds.add(inputVehicleId);
                }
            }

            if (!properVehicleIds.isEmpty()) {
                vehicleIds = properVehicleIds.toArray(new Long[0]);
            } else {
                vehicleIds = null;
            }
        }

        //start and end
        LocalDate startDate = CustomFormatter.stringToDate(start);
        LocalDate endDate = CustomFormatter.stringToDate(end);
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IncorrectRequestException("- start cannot be after end");
        }
        //end of checks

        BooleanExpression byParameters =
                WorkQueryCreator.createBooleanExpression(
                        userId, text, vehicleIds, categoryIds, isPlanned, startDate, endDate);

        Sort sort = WorkSorter.createSort(sortBy);
        PageRequest pageRequest = PageRequestCreator.create(from, size, sort);

        Iterable<Work> foundWorks = workRepository.findAll(byParameters, pageRequest);

        List<WorkFullDto> listToReturn = WorkMapper.workToFullDto(foundWorks);

        log.info("-- Works list by parameters returned, size={}", listToReturn.size());

        return listToReturn;
    }

    @Override
    @Transactional
    public void removeById(Long userId, Long workId) {

        log.info("--- Deleting work by workId={}", workId);

        WorkFullDto dtoToShowInLog = getById(userId, workId);

        workRepository.deleteById(workId);

        log.info("--- Work deleted: {}", dtoToShowInLog);
    }
}
