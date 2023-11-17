package com.mynerdygarage.work.service.util;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.work.dto.WorkFullDto;
import com.mynerdygarage.work.repository.WorkRepository;

import java.time.LocalDate;

public class WorkChecker {

    public static void check(WorkRepository workRepository, WorkFullDto workFullDto) {

        LocalDate startDate = CustomFormatter.stringToDate(workFullDto.getStartDate());
        LocalDate endDate = CustomFormatter.stringToDate(workFullDto.getEndDate());

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ConflictOnRequestException("- Start date cannot be after end date");
        }

        if (startDate != null && startDate.isBefore(LocalDate.now()) && workFullDto.getIsPlanned()) {
            throw new ConflictOnRequestException("- If isPlanned start date must be after current date");
        }

        if (workRepository.existsByVehicleIdAndStartDateAndTitleIgnoreCase(
                workFullDto.getVehicle().getId(), startDate, workFullDto.getTitle())) {
            throw new ConflictOnRequestException("- Work with this VehicleId and StartDate and Title already exists");
        }
    }
}
