package com.example.foodtracker.unit_tests.mealPlan;

import static org.junit.Assert.assertEquals;

import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.mealPlan.MealPlanDay;
import com.example.foodtracker.model.recipe.Recipe;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class MealPlanDayUnitTest {
    public ArrayList<Ingredient> getMockIngredientArrayList(){
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setDescription("Peanut Butter");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setDescription("Jelly");
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        ingredientArrayList.add(ingredient1);
        ingredientArrayList.add(ingredient2);
        return ingredientArrayList;
    }

    public ArrayList<Recipe> getMockRecipeArrayList(){
        Recipe recipe1 = new Recipe();
        recipe1.setTitle("PB&J");
        Recipe recipe2 = new Recipe();
        recipe2.setTitle("PB");
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        recipeArrayList.add(recipe1);
        recipeArrayList.add(recipe2);
        return recipeArrayList;
    }

    public MealPlanDay getMockMealPlanDay(){
        ArrayList<Ingredient> ingredientArrayList = getMockIngredientArrayList();
        ArrayList<Recipe> recipeArrayList = getMockRecipeArrayList();
        MealPlanDay mealPlanDay = new MealPlanDay("Monday", ingredientArrayList, recipeArrayList);
        return mealPlanDay;
    }

    @Test
    public void testConstructor(){
        MealPlanDay mealPlanDay = getMockMealPlanDay();
        assertEquals(mealPlanDay.getDay(), "Monday");
        assertEquals(mealPlanDay.getIngredients().get(0).getDescription(), "Peanut Butter");
        assertEquals(mealPlanDay.getIngredients().get(1).getDescription(), "Jelly");
        assertEquals(mealPlanDay.getRecipes().get(0).getTitle(), "PB&J");
        assertEquals(mealPlanDay.getRecipes().get(1).getTitle(), "PB");
    }

    @Test
    public void testSetAndGetDay(){
        MealPlanDay mealPlanDay = new MealPlanDay();
        mealPlanDay.setDay("Monday");
        assertEquals(mealPlanDay.getDay(), "Monday");
    }

    @Test
    public void testSetAndGetIngredients(){
        MealPlanDay mealPlanDay = new MealPlanDay();
        mealPlanDay.setIngredients(getMockIngredientArrayList());
        assertEquals(mealPlanDay.getIngredients().get(0).getDescription(), "Peanut Butter");
        assertEquals(mealPlanDay.getIngredients().get(1).getDescription(), "Jelly");
    }

    @Test
    public void testSetAndGetRecipes(){
        MealPlanDay mealPlanDay = new MealPlanDay();
        mealPlanDay.setRecipes(getMockRecipeArrayList());
        assertEquals(mealPlanDay.getRecipes().get(0).getTitle(), "PB&J");
        assertEquals(mealPlanDay.getRecipes().get(1).getTitle(), "PB");
    }

    @Test
    public void testGetCollectionName(){
        MealPlanDay mealPlanDay = new MealPlanDay();
        assertEquals(mealPlanDay.getCollectionName(), "MealPlan");
    }

    @Test
    public void testGetData(){
        MealPlanDay mealPlanDay = getMockMealPlanDay();
        Map<String, Object> data = mealPlanDay.getData();
        assertEquals(data.get("day"), "Monday");
        ArrayList<Ingredient> ingredientArrayList = (ArrayList<Ingredient>) data.get("ingredients");
        assertEquals(ingredientArrayList.get(0).getDescription(), "Peanut Butter");
        assertEquals(ingredientArrayList.get(1).getDescription(), "Jelly");
        ArrayList<Recipe> recipeArrayList = (ArrayList<Recipe>) data.get("recipes");
        assertEquals(recipeArrayList.get(0).getTitle(), "PB&J");
        assertEquals(recipeArrayList.get(1).getTitle(), "PB");
    }
}
