package com.example.foodtracker.model.IngredientUnit;

import java.io.Serializable;

/**
 * Represents an ingredient quantity
 * i.e. 3 KG, or 500 g
 */
public class IngredientAmount implements Serializable {

    double amount = 0;
    IngredientUnit unit;

    public IngredientAmount() {
    }

    public IngredientAmount(IngredientUnit unit, double amount) {
        this.setUnit(unit);
        this.setAmount(amount);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount >= 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public IngredientUnit getUnit() {
        return unit;
    }

    public void setUnit(IngredientUnit unit) {
        this.unit = unit;
    }
}
