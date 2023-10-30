package com.mynerdygarage.category.model.dafault;

import com.mynerdygarage.category.model.Category;

import java.util.ArrayList;
import java.util.List;

public class DefaultCategoriesHandler {

    private static List<Category> defaultCategoriesList;

    public static List<Category> getDefaultCategories() {

        if (defaultCategoriesList == null) {
            setDefaultCategories();
        }

        return defaultCategoriesList;
    }

    private static void setDefaultCategories() {

        defaultCategoriesList = new ArrayList<>();

        DefaultCategory engineCategory = new DefaultCategory(
                DefaultCategories.ENGINE,
                "Engine oil, oil filter, spark plugs etc."
        );
        defaultCategoriesList.add(engineCategory.getInstance());

        DefaultCategory gearboxCategory = new DefaultCategory(
                DefaultCategories.GEARBOX,
                "Gearbox oil, filters etc."
        );
        defaultCategoriesList.add(gearboxCategory.getInstance());

        DefaultCategory bodyCategory = new DefaultCategory(
                DefaultCategories.BODY,
                "All about bodywork - washing, detailing, doors, windows etc."
        );
        defaultCategoriesList.add(bodyCategory.getInstance());

        DefaultCategory chassisCategory = new DefaultCategory(
                DefaultCategories.CHASSIS,
                "Suspension, arms, stocks etc."
        );
        defaultCategoriesList.add(chassisCategory.getInstance());

        DefaultCategory brakesCategory = new DefaultCategory(
                DefaultCategories.BRAKES,
                "All about brakes"
        );
        defaultCategoriesList.add(brakesCategory.getInstance());

        DefaultCategory cabinCategory = new DefaultCategory(
                DefaultCategories.CABIN,
                "All about cabin works and parts"
        );
        defaultCategoriesList.add(cabinCategory.getInstance());

        DefaultCategory electricityCategory = new DefaultCategory(
                DefaultCategories.ELECTRICITY,
                "All about electric works and spares"
        );
        defaultCategoriesList.add(electricityCategory.getInstance());

        DefaultCategory wheelsCategory = new DefaultCategory(
                DefaultCategories.WHEELS,
                "Rims, tires etc."
        );
        defaultCategoriesList.add(wheelsCategory.getInstance());

        DefaultCategory otherCategory = new DefaultCategory(
                DefaultCategories.OTHER,
                "anything that doesn't match the existing categories"
        );
        defaultCategoriesList.add(otherCategory.getInstance());
    }
}
