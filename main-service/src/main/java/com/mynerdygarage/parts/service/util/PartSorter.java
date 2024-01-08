package com.mynerdygarage.parts.service.util;

import com.mynerdygarage.error.exception.NotFoundException;
import org.springframework.data.domain.Sort;

public class PartSorter {

    public static Sort createSort(String sortBy) {

        if (sortBy.equalsIgnoreCase(PartSortOptions.ID.name())) {
            sortBy = "id";
        } else if (sortBy.equalsIgnoreCase(PartSortOptions.CATEGORY.name())) {
            sortBy = "category";
        } else if (sortBy.equalsIgnoreCase(PartSortOptions.NAME.name())) {
            sortBy = "name";
        } else if (sortBy.equalsIgnoreCase(PartSortOptions.IS_REUSABLE.name())) {
            sortBy = "isReusable";
        } else if (sortBy.equalsIgnoreCase(PartSortOptions.STATUS.name())) {
            sortBy = "status";
        } else if (sortBy.equalsIgnoreCase(PartSortOptions.ORDER_DATE.name())) {
            sortBy = "orderDate";
        } else if (sortBy.equalsIgnoreCase(PartSortOptions.DELIVERY_DATE.name())) {
            sortBy = "deliveryDate";
        } else {
            throw new NotFoundException("- Sort option not found: " + sortBy);
        }

        return Sort.by(sortBy).and(Sort.by("id"));
    }
}
