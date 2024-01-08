package com.mynerdygarage.parts.dto;

import com.mynerdygarage.category.dto.CategoryMapper;
import com.mynerdygarage.parts.model.Part;
import com.mynerdygarage.user.dto.UserMapper;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.dto.VehicleMapper;

import java.util.ArrayList;
import java.util.List;

public class PartMapper {

    public static PartFullDto modelToFullDto(Part part) {

        return new PartFullDto(

                part.getId(),
                UserMapper.modelToShortDto(part.getOwner()),
                VehicleMapper.modelToShortDto(part.getVehicle()),
                CategoryMapper.modelToShortDto(part.getCategory()),
                part.getPartNumber(),
                part.getName(),
                part.getDescription(),
                part.getIsReusable(),
                part.getStatus(),
                CustomFormatter.dateToString(part.getOrderDate()),
                CustomFormatter.dateToString(part.getDeliveryDate())
        );
    }

    public static List<PartFullDto> modelToFullDto(Iterable<Part> parts) {

        List<PartFullDto> listToReturn = new ArrayList<>();

        for (Part part : parts) {
            listToReturn.add(modelToFullDto(part));
        }

        return listToReturn;
    }

    public static PartShortDto modelToShortDto(Part part) {

        return new PartShortDto(

                part.getId(),
                part.getPartNumber(),
                part.getName()
        );
    }

    public static List<PartShortDto> modelToShortDto(Iterable<Part> parts) {

        List<PartShortDto> listToReturn = new ArrayList<>();

        for (Part part : parts) {
            listToReturn.add(modelToShortDto(part));
        }

        return listToReturn;
    }
}
