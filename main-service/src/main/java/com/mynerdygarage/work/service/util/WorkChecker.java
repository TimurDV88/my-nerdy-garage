package com.mynerdygarage.work.service.util;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.part.model.PartStatus;
import com.mynerdygarage.work.model.Work;
import com.mynerdygarage.work.model.WorkStatus;
import com.mynerdygarage.work.repository.WorkRepository;

import java.time.LocalDate;
import java.util.Arrays;

public class WorkChecker {

    private static void check(Work workToCheck) {

        if (workToCheck.getStatus() != null &&
                !Arrays.stream(WorkStatus.values()).map(Enum::name).toList().contains(workToCheck.getStatus())) {

            throw new ConflictOnRequestException("- Incorrect work status");
        }

        LocalDate startDate = workToCheck.getStartDate();
        LocalDate endDate = workToCheck.getEndDate();
        boolean isPlanned;
        isPlanned = workToCheck.getStatus() != null && workToCheck.getStatus().equals(PartStatus.PLANNED.name());

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ConflictOnRequestException("- Start date cannot be after end date");
        }

        if (startDate != null && startDate.isBefore(LocalDate.now()) && isPlanned) {
            throw new ConflictOnRequestException("- If status='PLANNED' start date must be after current date");
        }
    }

    public static void checkNewWork(WorkRepository workRepository, Work newWorkToCheck) {

        check(newWorkToCheck);

        Work foundWork = workRepository.findByVehicleIdAndStartDateAndTitleIgnoreCase(
                newWorkToCheck.getVehicle().getId(),
                newWorkToCheck.getStartDate(),
                newWorkToCheck.getTitle());

        if (foundWork != null) {
            throw new ConflictOnRequestException("- Work with this VehicleId and StartDate and Title already exists");
        }
    }

    public static void checkUpdateWork(WorkRepository workRepository, Work updatedWorkToCheck) {

        check(updatedWorkToCheck);

        Work foundWork = workRepository.findByVehicleIdAndStartDateAndTitleIgnoreCase(
                updatedWorkToCheck.getVehicle().getId(),
                updatedWorkToCheck.getStartDate(),
                updatedWorkToCheck.getTitle()
        );

        if (!foundWork.getId().equals(updatedWorkToCheck.getId())) {
            throw new ConflictOnRequestException(
                    "- Another work with this VehicleId and StartDate and Title already exists");
        }
    }
}
