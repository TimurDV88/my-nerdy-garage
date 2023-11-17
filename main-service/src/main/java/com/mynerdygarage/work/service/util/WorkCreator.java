package com.mynerdygarage.work.service.util;

import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.model.Vehicle;
import com.mynerdygarage.work.dto.NewWorkDto;
import com.mynerdygarage.work.model.Work;

public class WorkCreator {

    public static Work create(User user, Vehicle vehicle, Category category, NewWorkDto newWorkDto) {

        Work work = new Work();

        work.setUser(user);
        work.setVehicle(vehicle);
        work.setCategory(category);
        work.setTitle(newWorkDto.getTitle());
        work.setDescription(newWorkDto.getDescription());
        work.setIsPlanned(newWorkDto.getIsPlanned());
        work.setStartDate(CustomFormatter.stringToDate(newWorkDto.getStartDate()));
        work.setEndDate(CustomFormatter.stringToDate(newWorkDto.getEndDate()));

        return work;
    }
}
