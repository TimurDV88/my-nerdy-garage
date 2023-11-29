package com.mynerdygarage.work.service.util;

import com.mynerdygarage.error.exception.NotFoundException;
import org.springframework.data.domain.Sort;

public class WorkSorter {

    public static Sort createSort(String sortBy) {

        if (sortBy.equalsIgnoreCase(WorkSortOptions.ID.name())) {
            sortBy = "id";
        } else if (sortBy.equalsIgnoreCase(WorkSortOptions.CATEGORY.name())) {
            sortBy = "category";
        } else if (sortBy.equalsIgnoreCase(WorkSortOptions.TITLE.name())) {
            sortBy = "title";
        } else if (sortBy.equalsIgnoreCase(WorkSortOptions.IS_PLANNED.name())) {
            sortBy = "isPlanned";
        } else if (sortBy.equalsIgnoreCase(WorkSortOptions.START_DATE.name())) {
            sortBy = "startDate";
        } else if (sortBy.equalsIgnoreCase(WorkSortOptions.END_DATE.name())) {
            sortBy = "endDate";
        } else {
            throw new NotFoundException("- Sort option not found: " + sortBy);
        }

        return Sort.by("vehicle").and(Sort.by(sortBy)).and(Sort.by("id"));
    }
}
