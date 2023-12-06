package com.example.foodtracker.unit_tests.recipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.foodtracker.model.recipe.Recipe;
import com.example.foodtracker.model.recipe.SimpleIngredient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class RecipeUnitTest {
    public Recipe getMockRecipe(){
        Recipe recipe = new Recipe();
        return recipe;
    }

    public SimpleIngredient getMockSimpleIngredient(String description){
        SimpleIngredient simpleIngredient = new SimpleIngredient();
        simpleIngredient.setDescription(description);
        return simpleIngredient;
    }

    public ArrayList<SimpleIngredient> getMockSimpleIngredientArrayList(){
        SimpleIngredient mockIngredient1 = getMockSimpleIngredient("Salt");
        SimpleIngredient mockIngredient2 = getMockSimpleIngredient("Sugar");
        SimpleIngredient mockIngredient3 = getMockSimpleIngredient("Pepper");
        ArrayList<SimpleIngredient> simpleIngredientArrayList1 = new ArrayList<>();
        simpleIngredientArrayList1.add(mockIngredient1);
        simpleIngredientArrayList1.add(mockIngredient2);
        simpleIngredientArrayList1.add(mockIngredient3);
        return simpleIngredientArrayList1;
    }

    @Test
    public void testSetAndGetTitle(){
        Recipe mockRecipe = getMockRecipe();
        mockRecipe.setTitle("PB&J");
        assertTrue(mockRecipe.getTitle() == "PB&J");
    }

    @Test
    public void testSetAndGetPrepTime(){
        Recipe mockRecipe = getMockRecipe();
        mockRecipe.setPrepTime(10);
        assertTrue(mockRecipe.getPrepTime() == 10);
    }

    @Test
    public void testSetAndGetServings(){
        Recipe mockRecipe = getMockRecipe();
        mockRecipe.setServings(2);
        assertTrue(mockRecipe.getServings() == 2);
    }

    @Test
    public void testSetAndGetCategory(){
        Recipe mockRecipe = getMockRecipe();
        mockRecipe.setCategory("Snack");
        assertTrue(mockRecipe.getCategory() == "Snack");
    }

    @Test
    public void testSetAndGetImage(){
        Recipe mockRecipe = getMockRecipe();
        mockRecipe.setImage("123");
        assertTrue(mockRecipe.getImage() == "123");
    }

    @Test
    public void testSetAndGetComment(){
        Recipe mockRecipe = getMockRecipe();
        mockRecipe.setComment("Easy to make");
        assertTrue(mockRecipe.getComment() == "Easy to make");
    }

    @Test
    public void testSetAndGetIngredients(){
        ArrayList<SimpleIngredient> simpleIngredientArrayList1 = getMockSimpleIngredientArrayList();
        Recipe mockRecipe = getMockRecipe();
        mockRecipe.setIngredients(simpleIngredientArrayList1);

        ArrayList<SimpleIngredient> simpleIngredientArrayList2 = mockRecipe.getIngredients();
        for(int i = 0; i <=2; i++){
            assertEquals(simpleIngredientArrayList1.get(i).getDescription(),
                    simpleIngredientArrayList2.get(i).getDescription());
        }
    }

    @Test
    public void testGetData(){
        Recipe mockRecipe = getMockRecipe();
        mockRecipe.setTitle("PB&J");
        mockRecipe.setCategory("Snack");
        mockRecipe.setComment("Easy to make");
        mockRecipe.setImage("123");
        mockRecipe.setServings(2);
        mockRecipe.setPrepTime(5);
        mockRecipe.setIngredients(getMockSimpleIngredientArrayList());

        Map<String, Object> data = mockRecipe.getData();
        assertTrue(data.get("title") == "PB&J");
        assertTrue(data.get("image") == "123");
        assertTrue(data.get("category") == "Snack");
        assertTrue(data.get("comment") == "Easy to make");
        assertEquals(data.get("servings"), 2);
        assertEquals(data.get("prepTime"), 5);
        ArrayList<SimpleIngredient> simpleIngredientArrayList1 = (ArrayList<SimpleIngredient>) data.get("ingredients");
        ArrayList<SimpleIngredient> simpleIngredientArrayList2 = getMockSimpleIngredientArrayList();
        for(int i = 0; i <=2; i++){
            assertEquals(simpleIngredientArrayList1.get(i).getDescription(),
                    simpleIngredientArrayList2.get(i).getDescription());
        }
    }

    @Test
    public void testEquals(){
        Recipe mockRecipe1 = getMockRecipe();
        mockRecipe1.setKey("123");
        Recipe mockRecipe2 = getMockRecipe();
        mockRecipe2.setKey("456");

        SimpleIngredient simpleIngredient = new SimpleIngredient();
        Object o = null;

        assertFalse(mockRecipe1.equals(o));
        assertTrue(mockRecipe1.equals(mockRecipe1));
        assertFalse(mockRecipe1.equals(simpleIngredient));

        assertFalse(mockRecipe1.equals(mockRecipe2));
        mockRecipe2.setKey("123");
        assertTrue(mockRecipe1.equals(mockRecipe2));

    }
}
