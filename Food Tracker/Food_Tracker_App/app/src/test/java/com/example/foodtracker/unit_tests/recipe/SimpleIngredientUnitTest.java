package com.example.foodtracker.unit_tests.recipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.foodtracker.model.IngredientUnit.IngredientAmount;
import com.example.foodtracker.model.IngredientUnit.IngredientUnit;
import com.example.foodtracker.model.ingredient.Category;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.recipe.SimpleIngredient;

import org.junit.Test;

import java.util.Objects;

public class SimpleIngredientUnitTest {
    public SimpleIngredient getMockSimpleIngredient(){
        SimpleIngredient simpleIngredient = new SimpleIngredient();
        return simpleIngredient;
    }

    @Test
    public void testConstructor(){
        Ingredient mockIngredient = new Ingredient();
        mockIngredient.setDescription("Salt");
        mockIngredient.setAmount(300);
        mockIngredient.setUnit("GRAM");
        mockIngredient.setCategory("Condiment");
        SimpleIngredient mockSimpleIngredient = new SimpleIngredient(mockIngredient);
        assertEquals(mockSimpleIngredient.getDescription(), "Salt");
        assertTrue(mockSimpleIngredient.getAmountQuantity() == 300.0);
        assertEquals(mockSimpleIngredient.getUnit(), "GRAM");
        assertEquals(mockSimpleIngredient.getCategoryName(), "Condiment");
    }

    @Test
    public void testSetAndGetDescription(){
        SimpleIngredient mockSimpleIngredient = getMockSimpleIngredient();
        mockSimpleIngredient.setDescription("Apple");
        assertTrue(mockSimpleIngredient.getDescription() == "Apple");
    }

    @Test
    public void testSetAndGetAmountQuantity(){
        SimpleIngredient mockSimpleIngredient = getMockSimpleIngredient();
        mockSimpleIngredient.setAmountQuantity(350);
        assertTrue(mockSimpleIngredient.getAmountQuantity() == 350);
    }

    @Test
    public void testSetAndGetUnit(){
        SimpleIngredient mockSimpleIngredient = getMockSimpleIngredient();
        mockSimpleIngredient.setUnit("GRAM");
        assertTrue(mockSimpleIngredient.getUnit() == "GRAM");
    }

    @Test
    public void testSetAndGetIngredientAmount(){
        SimpleIngredient mockSimpleIngredient = getMockSimpleIngredient();
        IngredientAmount ingredientAmount = new IngredientAmount(IngredientUnit.GRAM, 300);
        mockSimpleIngredient.setIngredientAmount(ingredientAmount);
        assertTrue(mockSimpleIngredient.getIngredientAmount().getAmount() == 300.0);
        assertEquals(mockSimpleIngredient.getIngredientAmount().getUnit(), IngredientUnit.GRAM);
    }

    @Test
    public void testSetAndGetCategoryName(){
        SimpleIngredient mockSimpleIngredient = getMockSimpleIngredient();
        mockSimpleIngredient.setCategoryName("Condiment");
        assertEquals(mockSimpleIngredient.getCategoryName(), "Condiment");
    }

    @Test
    public void testSetAndGetCategory(){
        SimpleIngredient mockSimpleIngredient = getMockSimpleIngredient();
        Category category = new Category("Condiment");
        mockSimpleIngredient.setCategory(category);
        assertEquals(mockSimpleIngredient.getCategory().getName(), "Condiment");
    }

    @Test
    public void testHashCode(){
        SimpleIngredient mockSimpleIngredient = getMockSimpleIngredient();
        mockSimpleIngredient.setDescription("Salt");
        assertEquals(mockSimpleIngredient.hashCode(), Objects.hash("Salt"));
    }

    @Test
    public void testEquals(){
        SimpleIngredient mockSimpleIngredient = getMockSimpleIngredient();
        mockSimpleIngredient.setDescription("Salt");
        SimpleIngredient mockSimpleIngredient2 = getMockSimpleIngredient();
        mockSimpleIngredient2.setDescription("Sugar");
        assertTrue(mockSimpleIngredient.equals(mockSimpleIngredient));
        assertFalse(mockSimpleIngredient.equals(mockSimpleIngredient2));
        mockSimpleIngredient2.setDescription("Salt");
        assertTrue(mockSimpleIngredient.equals(mockSimpleIngredient2));

        Category category = new Category();
        assertFalse(mockSimpleIngredient.equals(category));
    }


}
