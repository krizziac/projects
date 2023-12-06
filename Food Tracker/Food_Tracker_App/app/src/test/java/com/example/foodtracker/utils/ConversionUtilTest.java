package com.example.foodtracker.utils;

import static org.junit.Assert.*;

import com.example.foodtracker.model.IngredientUnit.IngredientAmount;
import com.example.foodtracker.model.IngredientUnit.IngredientUnit;

import org.junit.Test;

public class ConversionUtilTest {

    @Test
    public void convertAmount_kgToGram() {
        IngredientAmount ingredientAmountA = new IngredientAmount(IngredientUnit.KILOGRAM, 2);

        IngredientAmount converted = ConversionUtil.convertAmount(ingredientAmountA, IngredientUnit.GRAM);

        assertEquals(2000.0, converted.getAmount(), 0.1);
        assertEquals(IngredientUnit.GRAM, converted.getUnit());
    }

    @Test
    public void convertAmount_gToKg() {
        IngredientAmount ingredientAmountA = new IngredientAmount(IngredientUnit.GRAM, 500);

        IngredientAmount converted = ConversionUtil.convertAmount(ingredientAmountA, IngredientUnit.KILOGRAM);

        assertEquals(0.5, converted.getAmount(), 0.01);
        assertEquals(IngredientUnit.KILOGRAM, converted.getUnit());
    }

    @Test
    public void convertAmount_gramToGram() {
        IngredientAmount ingredientAmountA = new IngredientAmount(IngredientUnit.GRAM, 500);

        IngredientAmount converted = ConversionUtil.convertAmount(ingredientAmountA, IngredientUnit.GRAM);

        assertEquals(500, converted.getAmount(), 0.01);
        assertEquals(IngredientUnit.GRAM, converted.getUnit());
    }

    @Test
    public void convertAmount_gramToPound() {
        IngredientAmount ingredientAmountA = new IngredientAmount(IngredientUnit.GRAM, 500);

        IngredientAmount converted = ConversionUtil.convertAmount(ingredientAmountA, IngredientUnit.POUND);

        assertEquals(1.10231, converted.getAmount(), 0.01);
        assertEquals(IngredientUnit.POUND, converted.getUnit());
    }

    @Test
    public void getMissingAmount1() {
        // 1 Pound is equal to 16 ounces, so if we have 5 ounces we should require 11 more which in pounds is 0.6875 lbs
        IngredientAmount ingredientAmountOwned = new IngredientAmount(IngredientUnit.OUNCE, 5);
        IngredientAmount ingredientAmountNeeded = new IngredientAmount(IngredientUnit.POUND, 1);

        IngredientAmount needed = ConversionUtil.getMissingAmount(ingredientAmountOwned, ingredientAmountNeeded);

        assertEquals(0.6875, needed.getAmount(), 0.01);
        assertEquals(IngredientUnit.POUND, needed.getUnit());
    }

    @Test
    public void getMissingAmount2() {
        // 2kg is equal to 4.40925 lbs, so we have 3.4095 lbs over, and should expect that we need no more
        IngredientAmount ingredientAmountOwned = new IngredientAmount(IngredientUnit.KILOGRAM, 2);
        IngredientAmount ingredientAmountNeeded = new IngredientAmount(IngredientUnit.POUND, 1);

        IngredientAmount needed = ConversionUtil.getMissingAmount(ingredientAmountOwned, ingredientAmountNeeded);

        assertEquals(0, needed.getAmount(), 0.01);
        assertEquals(IngredientUnit.POUND, needed.getUnit());
    }

    @Test
    public void getMissingAmount3() {
        // 2.4L is equal to 81.1537 ounces, so we need 38.8463 more ounces
        IngredientAmount ingredientAmountOwned = new IngredientAmount(IngredientUnit.LITRE, 2.4);
        IngredientAmount ingredientAmountNeeded = new IngredientAmount(IngredientUnit.OUNCE, 120);

        IngredientAmount needed = ConversionUtil.getMissingAmount(ingredientAmountOwned, ingredientAmountNeeded);

        assertEquals(38.8463, needed.getAmount(), 0.01);
        assertEquals(IngredientUnit.OUNCE, needed.getUnit());
    }

    @Test
    public void convertAmount_poundToOunceToMillilitre() {
        // not super accurate but fun to see
        IngredientAmount pounds = new IngredientAmount(IngredientUnit.POUND, 150);

        IngredientAmount result = ConversionUtil.convertAmount(ConversionUtil.convertAmount(pounds, IngredientUnit.OUNCE), IngredientUnit.MILLILITRE);
        assertEquals(70976.472, result.getAmount(), 15);
    }
}