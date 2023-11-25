package com.mynerdygarage.work.service.util;

import com.mynerdygarage.util.NullChecker;
import com.mynerdygarage.work.model.Work;

public class WorkUpdater {

    public static Work update(Work workToUpdate, Work inputWork) {

        NullChecker.setIfNotNull(workToUpdate::setTitle, inputWork.getTitle());
        NullChecker.setIfNotNull(workToUpdate::setDescription, inputWork.getDescription());
        NullChecker.setIfNotNull(workToUpdate::setCategory, inputWork.getCategory());
        NullChecker.setIfNotNull(workToUpdate::setIsPlanned, inputWork.getIsPlanned());
        NullChecker.setIfNotNull(workToUpdate::setStartDate, inputWork.getStartDate());
        NullChecker.setIfNotNull(workToUpdate::setEndDate, inputWork.getEndDate());

        return workToUpdate;
    }
}
