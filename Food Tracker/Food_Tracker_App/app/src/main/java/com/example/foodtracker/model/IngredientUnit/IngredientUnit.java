package com.example.foodtracker.model.IngredientUnit;

public enum IngredientUnit {
    KILOGRAM("KG"),
    GRAM("G"),
    OUNCE("OZ"),
    POUND("LBS"),
    CUP("CUP"),
    TEASPOON("TSP"),
    TABLESPOON("TBSP"),
    LITRE("L"),
    MILLILITRE("ML"),
    UNIT("UNIT");

    final String unitAbbreviation;

    IngredientUnit(String unitAbbreviation) {
        this.unitAbbreviation = unitAbbreviation;
    }

    public String getUnitAbbreviation() {
        return unitAbbreviation;
    }
}

