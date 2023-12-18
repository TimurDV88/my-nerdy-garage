package com.mynerdygarage.work.service.util;

import com.mynerdygarage.work.model.QWork;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;

public class WorkQueryCreator {

    public static BooleanExpression createBooleanExpression(Long userId,
                                                            String text,
                                                            Long[] vehicleIds,
                                                            Long[] categoryIds,
                                                            String status,
                                                            LocalDate start,
                                                            LocalDate end) {

        //userId
        BooleanExpression byUserId = QWork.work.user.id.eq(userId);

        //text
        BooleanExpression byText = null;
        if (text != null) {
            byText = QWork.work.title.containsIgnoreCase(text)
                    .or(QWork.work.description.containsIgnoreCase(text));
        }

        //vehicleIds
        BooleanExpression byVehicleIds = null;
        if (vehicleIds != null) {
            byVehicleIds = QWork.work.vehicle.id.in(vehicleIds);
        }

        //categoryIds
        BooleanExpression byCategoryIds = null;
        if (categoryIds != null) {
            byCategoryIds = QWork.work.category.id.in(categoryIds);
        }

        //status
        BooleanExpression byStatus = null;
        if (status != null) {
            byStatus = QWork.work.status.eq(status);
        }

        //start
        BooleanExpression byStart = null;
        if (start != null) {
            byStart = QWork.work.startDate.goe(start);
        }

        //end
        BooleanExpression byEnd = null;
        if (end != null) {
            byEnd = QWork.work.endDate.loe(end);
        }

        return byUserId.and(byText).and(byVehicleIds).and(byCategoryIds).and(byStatus).and(byStart).and(byEnd);
    }
}
