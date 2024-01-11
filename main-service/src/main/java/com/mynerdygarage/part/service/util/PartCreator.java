package com.mynerdygarage.part.service.util;

import com.mynerdygarage.category.model.Category;
import com.mynerdygarage.part.dto.NewPartDto;
import com.mynerdygarage.part.dto.PartUpdateDto;
import com.mynerdygarage.part.model.Part;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.model.Vehicle;

public class PartCreator {

    public static Part createFromNewDto(User user, Vehicle vehicle, Category category, NewPartDto newPartDto) {

        Part part = new Part();

        part.setOwner(user);
        part.setVehicle(vehicle);
        part.setCategory(category);
        part.setPartNumber(newPartDto.getPartNumber());
        part.setName(newPartDto.getName());
        part.setDescription(newPartDto.getDescription());
        part.setIsReusable(newPartDto.getIsReusable());
        part.setStatus(newPartDto.getStatus());
        part.setOrderDate(CustomFormatter.stringToDate(newPartDto.getOrderDate()));
        part.setDeliveryDate(CustomFormatter.stringToDate(newPartDto.getDeliveryDate()));

        return part;
    }

    public static Part createFromUpdateDto(Vehicle vehicle, Category category, PartUpdateDto partUpdateDto) {

        Part part = new Part();

        part.setVehicle(vehicle);
        part.setCategory(category);
        part.setPartNumber(partUpdateDto.getPartNumber());
        part.setName(partUpdateDto.getName());
        part.setDescription(partUpdateDto.getDescription());
        part.setIsReusable(partUpdateDto.getIsReusable());
        part.setStatus(partUpdateDto.getStatus());
        part.setOrderDate(CustomFormatter.stringToDate(partUpdateDto.getOrderDate()));
        part.setDeliveryDate(CustomFormatter.stringToDate(partUpdateDto.getDeliveryDate()));

        return part;
    }
}
