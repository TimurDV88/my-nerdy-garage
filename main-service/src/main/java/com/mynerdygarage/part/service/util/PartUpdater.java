package com.mynerdygarage.part.service.util;

import com.mynerdygarage.part.model.Part;
import com.mynerdygarage.util.NullChecker;

public class PartUpdater {

    public static Part update(Part partToUpdate, Part inputPart) {

        NullChecker.setIfNotNull(partToUpdate::setVehicle, inputPart.getVehicle());
        NullChecker.setIfNotNull(partToUpdate::setCategory, inputPart.getCategory());
        NullChecker.setIfNotNull(partToUpdate::setPartNumber, inputPart.getPartNumber());
        NullChecker.setIfNotNull(partToUpdate::setName, inputPart.getName());
        NullChecker.setIfNotNull(partToUpdate::setDescription, inputPart.getDescription());
        NullChecker.setIfNotNull(partToUpdate::setIsReusable, inputPart.getIsReusable());
        NullChecker.setIfNotNull(partToUpdate::setStatus, inputPart.getStatus());
        NullChecker.setIfNotNull(partToUpdate::setOrderDate, inputPart.getOrderDate());
        NullChecker.setIfNotNull(partToUpdate::setDeliveryDate, inputPart.getDeliveryDate());

        return partToUpdate;
    }
}
