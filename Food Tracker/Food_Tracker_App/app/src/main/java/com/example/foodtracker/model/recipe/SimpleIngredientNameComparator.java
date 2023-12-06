package com.example.foodtracker.model.recipe;

import java.util.Comparator;

public class SimpleIngredientNameComparator implements Comparator<SimpleIngredient> {

    @Override
    public int compare(SimpleIngredient firstIngredient, SimpleIngredient secondIngredient) {
        if (!firstIngredient.getDescription().equals(secondIngredient.getDescription())){
            return firstIngredient.getDescription().compareTo(secondIngredient.getDescription());
        } else {
            return firstIngredient.getUnit().compareTo(secondIngredient.getUnit());
        }
    }
}