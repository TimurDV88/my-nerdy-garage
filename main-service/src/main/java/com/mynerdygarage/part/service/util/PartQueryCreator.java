package com.mynerdygarage.part.service.util;

import com.mynerdygarage.part.model.QPart;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;

public class PartQueryCreator {

    public static BooleanExpression createBooleanExpression(Long userId,
                                                            String text,
                                                            Long[] vehicleIds,
                                                            Long[] categoryIds,
                                                            Boolean isReusable,
                                                            String status,
                                                            LocalDate startDate,
                                                            LocalDate endDate) {

        //userId
        BooleanExpression byUserId = QPart.part.owner.id.eq(userId);

        //text
        BooleanExpression byText = null;
        if (text != null) {
            byText = QPart.part.name.containsIgnoreCase(text)
                    .or(QPart.part.description.containsIgnoreCase(text));
        }

        //vehicleIds
        BooleanExpression byVehicleIds = null;
        if (vehicleIds != null) {
            byVehicleIds = QPart.part.vehicle.id.in(vehicleIds);
        }

        //categoryIds
        BooleanExpression byCategoryIds = null;
        if (categoryIds != null) {
            byCategoryIds = QPart.part.category.id.in(categoryIds);
        }

        //categoryIds
        BooleanExpression byIsReusable = null;
        if (isReusable != null) {
            byIsReusable = QPart.part.isReusable.eq(isReusable);
        }

        //status
        BooleanExpression byStatus = null;
        if (status != null) {
            byStatus = QPart.part.status.eq(status);
        }

        //startDate
        BooleanExpression byStartDate = null;
        if (startDate != null) {
            byStartDate = QPart.part.orderDate.goe(startDate);
        }

        //endDate
        BooleanExpression byEndDate = null;
        if (endDate != null) {
            byEndDate = QPart.part.orderDate.loe(endDate);
        }

        return byUserId.and(byText).and(byVehicleIds).and(byCategoryIds).and(byIsReusable)
                .and(byStatus).and(byStartDate).and(byEndDate);
    }
}
