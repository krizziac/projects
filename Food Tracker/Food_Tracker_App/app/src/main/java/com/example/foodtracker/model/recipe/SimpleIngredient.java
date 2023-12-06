package com.example.foodtracker.model.recipe;

import com.example.foodtracker.model.IngredientUnit.IngredientAmount;
import com.example.foodtracker.model.IngredientUnit.IngredientUnit;
import com.example.foodtracker.model.ingredient.Category;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.utils.ConversionUtil;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents basic information about an ingredient
 */
public class SimpleIngredient implements Serializable {

    /**
     * A brief description of what the ingredient is
     */
    private String description = "";
    /**
     * The category that represents this ingredient (i.e. Fruit)
     */
    private Category category = new Category();
    /**
     * The amount that we have of this ingredient
     */
    private IngredientAmount ingredientAmount = new IngredientAmount();

    public SimpleIngredient() {
    }

    public SimpleIngredient(Ingredient ingredient) {
        setAmountQuantity(ingredient.getAmount());
        setUnit(ingredient.getUnit());
        setDescription(ingredient.getDescription());
        setCategoryName(ingredient.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleIngredient that = (SimpleIngredient) o;
        return getDescription().equalsIgnoreCase(that.getDescription()) && getUnit().equals(that.getUnit());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return this.category.getName();
    }

    public void setCategoryName(String category) {
        this.category.setName(category);
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getAmountQuantity() {
        return ingredientAmount.getAmount();
    }

    public void setAmountQuantity(double ingredientAmount) {
        this.ingredientAmount.setAmount(ingredientAmount);
    }

    public IngredientAmount getIngredientAmount() {
        return ingredientAmount;
    }

    public void setIngredientAmount(IngredientAmount amount) {
        this.ingredientAmount = amount;
    }

    public void addIngredientAmount(IngredientAmount toAdd) throws IllegalArgumentException {
        IngredientAmount converted = ConversionUtil.convertAmount(toAdd, this.ingredientAmount.getUnit());
        setAmountQuantity(getAmountQuantity() + converted.getAmount());
    }

    public String getUnit() {
        return ingredientAmount.getUnit().name();
    }

    public void setUnit(String unit) {
        try {
            this.ingredientAmount.setUnit(IngredientUnit.valueOf(unit));
        } catch (IllegalArgumentException illegalArgumentException) {
            // do nothing
        }
    }

    public String getUnitAbbreviation() {
        if (ingredientAmount.getUnit() != null) {
            return ingredientAmount.getUnit().getUnitAbbreviation();
        } else {
            return "NONE";
        }
    }
}
