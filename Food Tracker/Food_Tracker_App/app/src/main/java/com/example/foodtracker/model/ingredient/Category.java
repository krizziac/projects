package com.example.foodtracker.model.ingredient;

import com.example.foodtracker.model.Document;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Represents an ingredient category, extends Document since it is represented by a firestore collection
 */
public class Category extends Document {

    public static String INGREDIENTS_CATEGORY_COLLECTION_NAME = "Ingredients-Category";
    private String name = "";

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    @Override
    public String getCollectionName() {
        return INGREDIENTS_CATEGORY_COLLECTION_NAME;
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
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put(FieldNames.CATEGORY_NAME, name);
        return data;
    }

    @Override
    public boolean hasNonDefaultKey() {
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Contains the field names that are used to describe the object in our instance of firestore
     * these must match the names of the class fields and setters
     */
    public static class FieldNames {
        public static String CATEGORY_NAME = "name";
    }
}

