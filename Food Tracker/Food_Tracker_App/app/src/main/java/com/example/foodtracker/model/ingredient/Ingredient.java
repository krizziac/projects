package com.example.foodtracker.model.ingredient;


import com.example.foodtracker.model.Document;
import com.example.foodtracker.model.DocumentableFieldName;
import com.example.foodtracker.model.IngredientUnit.IngredientAmount;
import com.example.foodtracker.model.IngredientUnit.IngredientUnit;
import com.example.foodtracker.model.recipe.SimpleIngredient;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an ingredient in our storage
 * {@link Document} allows objects to be used as Firebase instances
 */
public class Ingredient extends Document {

    /**
     * Represents the collection name in Firebase
     */
    public static String INGREDIENTS_COLLECTION_NAME = "Ingredients-V1.0.0";
    /**
     * Represents the amount we have of this ingredient
     */
    private final IngredientAmount ingredientAmount = new IngredientAmount();
    /**
     * The name of the ingredient
     */
    private String description;
    /**
     * Represents where this ingredient is stored
     */
    private Location location = new Location();

    /**
     * Represents what category we lump this ingredient into, i.e. Fruit
     */
    private Category category = new Category();

    /**
     * Represents the expiry date of our ingredient
     */
    private String expiry = "";

    public Ingredient() {
    }

    public Ingredient(SimpleIngredient ingredient) {
        setAmount(ingredient.getAmountQuantity());
        setUnit(ingredient.getUnit());
        setDescription(ingredient.getDescription());
        setCategory(ingredient.getCategoryName());
    }

    @Override
    public String getCollectionName() {
        return INGREDIENTS_COLLECTION_NAME;
    }

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put(FieldName.DESCRIPTION.getName(), this.getDescription());
        data.put(FieldName.UNIT.getName(), this.getUnit());
        data.put(FieldName.LOCATION.getName(), this.getLocation());
        data.put(FieldName.CATEGORY.getName(), this.getCategory());
        data.put(FieldName.AMOUNT.getName(), this.getAmount());
        data.put(FieldName.EXPIRY.getName(), this.getExpiry());
        return data;
    }

    public double getAmount() {
        return ingredientAmount.getAmount();
    }

    public void setAmount(double amount) {
        this.ingredientAmount.setAmount(amount);
    }

    public String getCategory() {
        return category.getName();
    }

    public void setCategory(String category) {
        this.category = new Category(category);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location.getName();
    }

    public void setLocation(String location) {
        this.location = new Location(location);
    }

    public String getUnit() {
        return this.ingredientAmount.getUnit().toString();
    }

    public void setUnit(String unit) {
        this.ingredientAmount.setUnit(IngredientUnit.valueOf(unit));
    }

    public String getUnitAbbreviation() {
        return this.ingredientAmount.getUnit().getUnitAbbreviation();
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    /**
     * Returns true if certain fields are missing on the ingredient
     * notably checks the fields that will not be set when creating an ingredient from a {@link SimpleIngredient}
     */
    public boolean isMissingFields() {
        return location.getName().isEmpty() || expiry.isEmpty();
    }

    /**
     * Contains the names of the ingredient class fields
     */
    public enum FieldName implements DocumentableFieldName {
        DESCRIPTION("description", true),
        UNIT("unit", false),
        LOCATION("location", true),
        CATEGORY("category", true),
        AMOUNT("amount", false),
        EXPIRY("expiry", true);
        private final String name;
        private final boolean sortable;

        FieldName(String fieldName, boolean sortable) {
            this.name = fieldName;
            this.sortable = sortable;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean sortable() {
            return sortable;
        }
    }
}