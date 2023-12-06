package com.example.foodtracker.unit_tests.ingredient;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.foodtracker.model.IngredientUnit.IngredientAmount;
import com.example.foodtracker.model.IngredientUnit.IngredientUnit;

import org.junit.Test;

public class IngredientAmountUnitTest {
    public IngredientAmount getMockIngredientAmount(){
        IngredientAmount ingredientAmount = new IngredientAmount();
        return ingredientAmount;
    }
    public IngredientAmount getMockIngredientAmount(IngredientUnit unit, double amount){
        IngredientAmount ingredientAmount = new IngredientAmount(unit, amount);
        return ingredientAmount;
    }

    @Test
    public void testConstructor(){
        IngredientUnit ingredientUnit = IngredientUnit.GRAM;
        IngredientAmount mockIngredientAmount = new IngredientAmount(ingredientUnit, 350);
        assertTrue(mockIngredientAmount.getAmount() == 350);
        assertTrue(mockIngredientAmount.getUnit() == IngredientUnit.GRAM);
    }

    @Test
    public void testSetAndGetAmount(){
        IngredientAmount mockIngredientAmount = new IngredientAmount();
        mockIngredientAmount.setAmount(350);
        assertTrue(mockIngredientAmount.getAmount() == 350);
        assertThrows(IllegalArgumentException.class, () -> {mockIngredientAmount.setAmount(-5);});
    }

    @Test
    public void testSetAndGetUnit(){
        IngredientAmount mockIngredientAmount = new IngredientAmount();
        IngredientUnit ingredientUnit = IngredientUnit.GRAM;
        mockIngredientAmount.setUnit(ingredientUnit);
        assertTrue(mockIngredientAmount.getUnit() == IngredientUnit.GRAM);
    }


}
