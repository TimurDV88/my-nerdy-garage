package com.mynerdygarage.util;

import com.mynerdygarage.category.repository.CategoryRepository;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.vehicle.repository.VehicleRepository;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class RequestParamsChecker {

    private final VehicleRepository vehicleRepository;
    private final CategoryRepository categoryRepository;

    public String checkAndReturnText(String text) {

        if (text != null && (text.isBlank() || text.length() < 2)) {
            text = null;
        }

        return text;
    }

    public Long[] checkAndReturnVehicleIds(Long userId, Long[] vehicleIds) {

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

        return vehicleIds;
    }

    public Long[] checkAndReturnCategoryIds(Long userId, Long[] categoryIds) {

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

        return categoryIds;
    }

    public void checkStartAndEnd(LocalDate startDate, LocalDate endDate) {

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IncorrectRequestException("- start cannot be after end");
        }
    }
}
