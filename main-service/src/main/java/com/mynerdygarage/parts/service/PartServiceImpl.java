package com.mynerdygarage.parts.service;

import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.category.repository.CategoryRepository;
import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.parts.dto.NewPartDto;
import com.mynerdygarage.parts.dto.PartFullDto;
import com.mynerdygarage.parts.dto.PartMapper;
import com.mynerdygarage.parts.dto.PartUpdateDto;
import com.mynerdygarage.parts.model.Part;
import com.mynerdygarage.parts.model.PartStatus;
import com.mynerdygarage.parts.repository.PartRepository;
import com.mynerdygarage.parts.service.util.*;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.util.PageRequestCreator;
import com.mynerdygarage.vehicle.model.Vehicle;
import com.mynerdygarage.vehicle.repository.VehicleRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartServiceImpl {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final CategoryRepository categoryRepository;
    private final PartRepository partRepository;


    @Transactional
    public PartFullDto addPart(Long userId, NewPartDto newPartDto) {

        log.info("-- Saving part by user with Id={}: {}", userId, newPartDto);

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- UserId not found: " + userId));

        Vehicle vehicle = vehicleRepository.findById(newPartDto.getVehicleId()).orElseThrow(() ->
                new NotFoundException("- VehicleId not found: " + newPartDto.getVehicleId()));

        Category category = categoryRepository.findById(newPartDto.getCategoryId()).orElseThrow(() ->
                new NotFoundException("- CategoryId not found: " + newPartDto.getCategoryId()));

        Part part = PartCreator.createFromNewDto(user, vehicle, category, newPartDto);

        PartChecker.checkNewPart(partRepository, part);

        PartFullDto fullDtoToReturn = PartMapper.modelToFullDto(partRepository.save(part));

        log.info("-- Part has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Transactional
    public PartFullDto update(Long userId, Long partId, PartUpdateDto inputDto) {

        log.info("-- Updating part by partId={}: {}", partId, inputDto);

        Vehicle vehicle = vehicleRepository.findById(inputDto.getVehicleId()).orElseThrow(() ->
                new NotFoundException("- VehicleId not found: " + inputDto.getVehicleId()));

        Part partToUpdate = partRepository.findById(partId).orElseThrow(() ->
                new NotFoundException("- partId not found: " + partId));

        if (!userId.equals(partToUpdate.getUser().getId())) {
            throw new NotFoundException("- User with Id=" + userId + " is not buyer of part with id=" + partId);
        }

        Category category;
        if (inputDto.getCategoryId() != null) {
            category = categoryRepository.findById(inputDto.getCategoryId()).orElseThrow(() ->
                    new NotFoundException("- CategoryId not found: " + inputDto.getCategoryId()));
        } else {
            category = partToUpdate.getCategory();
        }

        Part inputPart = PartCreator.createFromUpdateDto(vehicle, category, inputDto);

        Part updatedPart = PartUpdater.update(partToUpdate, inputPart);

        PartChecker.checkUpdatePart(partRepository, updatedPart);

        PartFullDto fullDtoToReturn = PartMapper.modelToFullDto(partRepository.save(updatedPart));

        log.info("-- Work has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    public PartFullDto getById(Long userId, Long partId) {

        log.info("-- Returning part by partId={}", partId);

        PartFullDto fullDtoToReturn = PartMapper.modelToFullDto(partRepository.findById(partId).orElseThrow(() ->
                new NotFoundException("- partId not found: " + partId)));

        if (!userId.equals(fullDtoToReturn.getUserShortDto().getId())) {
            throw new NotFoundException("- User with Id=" + userId + " is not buyer of part with id=" + partId);
        }

        log.info("-- Part returned: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    public List<PartFullDto> getByParams(Long userId,
                                         String text,
                                         Long[] vehicleIds,
                                         Long[] categoryIds,
                                         Boolean isReusable,
                                         String status,
                                         String start,
                                         String end,
                                         String sortBy, int from, int size) {

        log.info("-- Returning part list by parameters: text={}, vehicleIds={}, categoryIds={}, status={}, " +
                        "startFromDate={}, endByDate={}, sort={}",
                text, vehicleIds, categoryIds, status, start, end, sortBy);

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

        // categoryIds
        if (categoryIds != null) {

            List<Long> existedCategoryIds = categoryRepository.findAvailableIdsByCreatorId(userId);
            List<Long> properCategoryIds = new ArrayList<>();

            for (Long inputCategoryId : categoryIds) {
                if (existedCategoryIds.contains(inputCategoryId)) {
                    properCategoryIds.add(inputCategoryId);
                }
            }

            if (!properCategoryIds.isEmpty()) {
                categoryIds = properCategoryIds.toArray(new Long[0]);
            } else {
                categoryIds = null;
            }
        }

        //status
        if (status != null &&
                !Arrays.stream(PartStatus.values()).map(Enum::name).toList().contains(status)) {

            throw new ConflictOnRequestException("- Incorrect part status");
        }

        //start and end
        LocalDate startDate = CustomFormatter.stringToDate(start);
        LocalDate endDate = CustomFormatter.stringToDate(end);
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IncorrectRequestException("- start cannot be after end");
        }
        //end of checks

        BooleanExpression byParameters =
                PartQueryCreator.createBooleanExpression(
                        userId, text, vehicleIds, categoryIds, isReusable, status, startDate, endDate);

        Sort sort = PartSorter.createSort(sortBy);
        PageRequest pageRequest = PageRequestCreator.create(from, size, sort);

        Iterable<Part> foundParts = partRepository.findAll(byParameters, pageRequest);

        List<PartFullDto> listToReturn = PartMapper.modelToFullDto(foundParts);

        log.info("-- Works list by parameters returned, size={}", listToReturn.size());

        return listToReturn;
    }
}
