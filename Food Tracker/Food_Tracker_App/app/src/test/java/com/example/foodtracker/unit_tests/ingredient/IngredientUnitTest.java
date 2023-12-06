package com.example.foodtracker.unit_tests.ingredient;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.recipe.SimpleIngredient;

import org.junit.Test;

import java.util.Map;

/**
 * Test to check the functionality of Ingredient class, it's attributes and functions
 */
public class IngredientUnitTest {
    /**
     * Function to return an Ingredient object
     * @return Object of type {@link Ingredient}
     */
    private Ingredient getMockIngredient(){
        Ingredient mockIngredient = new Ingredient();
        return mockIngredient;
    }


    @Test
    public void testSetAndGetAmount(){
        Ingredient mockIngredient = getMockIngredient();
        mockIngredient.setAmount(3.5);
        assertTrue(mockIngredient.getAmount() == 3.5);
    }

    @Test
    public void testSetAndGetUnit(){
        Ingredient mockIngredient = getMockIngredient();
        mockIngredient.setUnit("GRAM");
        assertTrue(mockIngredient.getUnit() == "GRAM");
    }

    @Test
    public void testSetAndGetLocation(){
        Ingredient mockIngredient = getMockIngredient();
        mockIngredient.setLocation("Pantry");
        assertTrue(mockIngredient.getLocation() == "Pantry");
    }

    @Test
    public void testSetAndGetDescription(){
        Ingredient mockIngredient = getMockIngredient();
        mockIngredient.setDescription("Salt");
        assertTrue(mockIngredient.getDescription() == "Salt");
    }

    @Test
    public void testSetAndGetCategory(){
        Ingredient mockIngredient = getMockIngredient();
        mockIngredient.setCategory("Fruit");
        assertTrue(mockIngredient.getCategory() == "Fruit");
    }

    @Test
    public void testSetAndGetExpiry(){
        Ingredient mockIngredient = getMockIngredient();
        mockIngredient.setExpiry("123");
        assertTrue(mockIngredient.getExpiry() == "123");
    }

    @Test
    public void testGetCollectionsName(){
        Ingredient mockIngredient = getMockIngredient();
        assertTrue(mockIngredient.getCollectionName() == "Ingredients-V1.0.0");
    }

    @Test
    public void testGetData(){
        Ingredient mockIngredient = new Ingredient();
        mockIngredient.setDescription("Apple");
        mockIngredient.setUnit("GRAM");
        mockIngredient.setAmount(350);
        mockIngredient.setCategory("Fruit");
        mockIngredient.setLocation("Pantry");
        mockIngredient.setExpiry("123");
        Map<String, Object> data = mockIngredient.getData();
        assertTrue(data.get("description") == "Apple");
        assertTrue(data.get("unit") == "GRAM");
        assertEquals(data.get("amount"), 350.0);
        assertTrue(data.get("category") == "Fruit");
        assertTrue(data.get("location") == "Pantry");
    }

    @Test
    public void testGetUnitAbbreviation(){
        Ingredient mockIngredient = new Ingredient();
        mockIngredient.setUnit("GRAM");
        assertEquals(mockIngredient.getUnitAbbreviation(), "G");
    }

    @Test
    public void testIsMissingFields(){
        Ingredient mockIngredient = new Ingredient();
        assertTrue(mockIngredient.isMissingFields());
        mockIngredient.setExpiry("123");
        assertTrue(mockIngredient.isMissingFields());
        mockIngredient.setLocation("Pantry");
        assertFalse(mockIngredient.isMissingFields());
    }
}
