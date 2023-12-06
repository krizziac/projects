package com.example.foodtracker.model.recipe;

import com.example.foodtracker.model.Document;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Represents an recipe category, extends Document since it is represented by a firestore collection
 */
public class Category extends Document {

    public static String RECIPE_CATEGORY_COLLECTION_NAME = "Recipes-Category";
    private String name;

    public Category() {
    }
    public Category(String name) {
        this.name = name;
    }

    @Override
    public String getCollectionName() {
        return RECIPE_CATEGORY_COLLECTION_NAME;
    }

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put(FieldNames.CATEGORY_NAME, name);
        return data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return getName().toUpperCase(Locale.ROOT);
    }

    @Override
    public void setKey(String key) {
        setName(key);
    }

    @Override
    public boolean hasNonDefaultKey() {
        return true;
    }

    /**
     * Contains the field names that are used to describe the object in our instance of firestore
     * these must match the names of the class fields and setters
     */
    public static class FieldNames {
        public static String CATEGORY_NAME = "name";
    }
}

