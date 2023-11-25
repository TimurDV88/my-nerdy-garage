package com.mynerdygarage.work.service.util;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.work.model.Work;
import com.mynerdygarage.work.repository.WorkRepository;

import java.time.LocalDate;

public class WorkChecker {

    private static void check(Work work) {

        LocalDate startDate = work.getStartDate();
        LocalDate endDate = work.getEndDate();

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ConflictOnRequestException("- Start date cannot be after end date");
        }

        if (startDate != null && startDate.isBefore(LocalDate.now()) && work.getIsPlanned()) {
            throw new ConflictOnRequestException("- If isPlanned start date must be after current date");
        }
    }

    public static void checkNewWork(WorkRepository workRepository, Work work) {

        check(work);

        if (workRepository.existsByVehicleIdAndStartDateAndTitleIgnoreCase(
                work.getVehicle().getId(), work.getStartDate(), work.getTitle())) {
            throw new ConflictOnRequestException("- Work with this VehicleId and StartDate and Title already exists");
        }
    }

    public static void checkUpdateWork(Work work) {

        check(work);
    }
}
