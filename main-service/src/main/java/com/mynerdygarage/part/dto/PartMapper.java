package com.mynerdygarage.part.dto;

import com.mynerdygarage.category.dto.CategoryMapper;
import com.mynerdygarage.part.model.Part;
import com.mynerdygarage.user.dto.UserMapper;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.vehicle.dto.VehicleMapper;

import java.util.List;
import java.util.stream.StreamSupport;

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

        return StreamSupport.stream(parts.spliterator(), false)
                .map(PartMapper::modelToFullDto)
                .toList();
    }

    public static PartShortDto modelToShortDto(Part part) {

        return new PartShortDto(

                part.getId(),
                part.getPartNumber(),
                part.getName()
        );
    }

    public static List<PartShortDto> modelToShortDto(Iterable<Part> parts) {

        return StreamSupport.stream(parts.spliterator(), false)
                .map(PartMapper::modelToShortDto)
                .toList();
    }
}
