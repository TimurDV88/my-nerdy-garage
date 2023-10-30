package com.mynerdygarage.category.model.dafault;

import com.mynerdygarage.category.model.Category;


public class DefaultCategory {

    private final DefaultCategories name;
    private final String description;
    private Category uniqueInstance;

    protected DefaultCategory(DefaultCategories name, String description) {

        this.name = name;
        this.description = description;
    }

    public Category getInstance() {

        if (uniqueInstance == null) {
            uniqueInstance = new Category();
            uniqueInstance.setName(name.toString());
            uniqueInstance.setDescription(description);
            uniqueInstance.setCreator(null);
        }
        return uniqueInstance;
    }
}
