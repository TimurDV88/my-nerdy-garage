package com.mynerdygarage.part.service;

import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.category.repository.CategoryRepository;
import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.part.dto.NewPartDto;
import com.mynerdygarage.part.dto.PartFullDto;
import com.mynerdygarage.part.dto.PartMapper;
import com.mynerdygarage.part.dto.PartUpdateDto;
import com.mynerdygarage.part.model.Part;
import com.mynerdygarage.part.model.PartStatus;
import com.mynerdygarage.part.repository.PartRepository;
import com.mynerdygarage.part.service.util.*;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.util.PageRequestCreator;
import com.mynerdygarage.util.RequestParamsChecker;
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
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final CategoryRepository categoryRepository;
    private final PartRepository partRepository;

    @Transactional
    @Override
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
    @Override
    public PartFullDto update(Long userId, Long partId, PartUpdateDto inputDto) {

        log.info("-- Updating part by partId={}: {}", partId, inputDto);

        Part partToUpdate = partRepository.findById(partId).orElseThrow(() ->
                new NotFoundException("- partId not found: " + partId));

        if (!userId.equals(partToUpdate.getOwner().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not owner of part with id=" + partId);
        }

        Vehicle vehicle;
        if (inputDto.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(inputDto.getVehicleId()).orElseThrow(() ->
                    new NotFoundException("- VehicleId not found: " + inputDto.getVehicleId()));
        } else {
            vehicle = partToUpdate.getVehicle();
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

        log.info("-- Part has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public PartFullDto getById(Long userId, Long partId) {

        log.info("-- Returning part by partId={}", partId);

        PartFullDto fullDtoToReturn = PartMapper.modelToFullDto(partRepository.findById(partId).orElseThrow(() ->
                new NotFoundException("- partId not found: " + partId)));

        if (!userId.equals(fullDtoToReturn.getUserShortDto().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not buyer of part with id=" + partId);
        }

        log.info("-- Part returned: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public List<PartFullDto> getByParams(Long userId,
                                         String text,
                                         Long[] vehicleIds,
                                         Long[] categoryIds,
                                         Boolean isReusable,
                                         String status,
                                         String start,
                                         String end,
                                         String sortBy, int from, int size) {

        log.info("-- Returning parts list by parameters: text={}, vehicleIds={}, categoryIds={}, status={}, " +
                        "startFromDate={}, endByDate={}, sort={}",
                text, vehicleIds, categoryIds, status, start, end, sortBy);

        // - checks:
        RequestParamsChecker requestParamsChecker = new RequestParamsChecker(vehicleRepository, categoryRepository);

        // text
        text = requestParamsChecker.checkAndReturnText(text);

        // vehicleIds
        vehicleIds = requestParamsChecker.checkAndReturnVehicleIds(userId, vehicleIds);

        // categoryIds
        categoryIds = requestParamsChecker.checkAndReturnCategoryIds(userId, categoryIds);

        // status
        if (status != null &&
                !Arrays.stream(PartStatus.values()).map(Enum::name).toList().contains(status)) {

            throw new ConflictOnRequestException("- Incorrect part status");
        }

        // start and end
        LocalDate startDate = CustomFormatter.stringToDate(start);
        LocalDate endDate = CustomFormatter.stringToDate(end);
        requestParamsChecker.checkStartAndEnd(startDate, endDate);

        // - end of checks

        BooleanExpression byParameters =
                PartQueryCreator.createBooleanExpression(
                        userId, text, vehicleIds, categoryIds, isReusable, status, startDate, endDate);

        Sort sort = PartSorter.createSort(sortBy);
        PageRequest pageRequest = PageRequestCreator.create(from, size, sort);

        Iterable<Part> foundParts = partRepository.findAll(byParameters, pageRequest);

        List<PartFullDto> listToReturn = PartMapper.modelToFullDto(foundParts);

        log.info("-- Part list by parameters returned, size={}", listToReturn.size());

        return listToReturn;
    }

    @Transactional
    @Override
    public void removeById(Long userId, Long partId) {

        log.info("--- Deleting part by partId={}", partId);

        PartFullDto dtoToShowInLog = getById(userId, partId);

        partRepository.deleteById(partId);

        log.info("--- Part deleted: {}", dtoToShowInLog);
    }
}
