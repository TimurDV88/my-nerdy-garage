package com.mynerdygarage.work.service.util;

import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.model.Vehicle;
import com.mynerdygarage.work.dto.NewWorkDto;
import com.mynerdygarage.work.dto.WorkUpdateDto;
import com.mynerdygarage.work.model.Work;

public class WorkCreator {

    public static Work createFromNewDto(User user, Vehicle vehicle, Category category, NewWorkDto newWorkDto) {

        Work work = new Work();

        work.setInitiator(user);
        work.setVehicle(vehicle);
        work.setCategory(category);
        work.setTitle(newWorkDto.getTitle());
        work.setDescription(newWorkDto.getDescription());
        work.setStatus(newWorkDto.getStatus());
        work.setStartDate(CustomFormatter.stringToDate(newWorkDto.getStartDate()));
        work.setEndDate(CustomFormatter.stringToDate(newWorkDto.getEndDate()));

        return work;
    }

    public static Work createFromUpdateDto(Category category, WorkUpdateDto workUpdateDto) {

        Work work = new Work();

        work.setCategory(category);
        work.setTitle(workUpdateDto.getTitle());
        work.setDescription(workUpdateDto.getDescription());
        work.setStatus(workUpdateDto.getStatus());
        work.setStartDate(CustomFormatter.stringToDate(workUpdateDto.getStartDate()));
        work.setEndDate(CustomFormatter.stringToDate(workUpdateDto.getEndDate()));

        return work;
    }
}
