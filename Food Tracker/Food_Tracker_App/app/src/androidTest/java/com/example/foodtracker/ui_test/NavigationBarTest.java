package com.example.foodtracker.ui_test;

import static org.junit.Assert.assertTrue;

import com.example.foodtracker.R;
import com.example.foodtracker.ui.mealPlan.MealPlanMainScreen;
import com.example.foodtracker.ui.recipes.RecipesMainScreen;
import com.example.foodtracker.ui.shoppingCart.ShoppingCartMainScreen;
import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.foodtracker.MainActivity;
import com.example.foodtracker.ui.ingredients.IngredientsMainScreen;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Class to test the functionality of the navigation bar at the bottom of the screen
 * @version 2.0
 */
public class NavigationBarTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityRule.getActivity());
    }

    /**
     * Check to see if clicking on Ingredients icon in the navigation bar works and brings us to Ingredients Main Screen
     */
    @Test
    public void clickOnIngredientsIconInNavBar(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_ingredients));
        assertTrue(solo.waitForActivity(IngredientsMainScreen.class));
    }

    /**
     * Check if clicking Shopping Cart Icon in Navigation Bar brings us to Shopping Cart Main Activity
     */
    @Test
    public void clickOnShoppingCartButtonInNavBar(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_shopping));
        assertTrue(solo.waitForActivity(ShoppingCartMainScreen.class));
    }

    /**
     * Check if clicking on Recipes Icon in Navigation Bar brings us to Recipes Main Activity
     */
    @Test
    public void clickOnRecipesButtonInNavBar(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_recipes));
        assertTrue(solo.waitForActivity(RecipesMainScreen.class));
    }

    /**
     * Check if clicking on Meal Plan Icon in Navigation Bar brings us to Meal Plan Main Activity
     */
    @Test
    public void clickOnMealPlanButtonInNavBar(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_meal_plan));
        assertTrue(solo.waitForActivity(MealPlanMainScreen.class));
    }

    @Test
    public void MoveBetweenActivities(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_ingredients));
        assertTrue(solo.waitForActivity(IngredientsMainScreen.class));
        solo.clickOnView(solo.getView(R.id.navigation_shopping));
        assertTrue(solo.waitForActivity(ShoppingCartMainScreen.class));
        solo.clickOnView(solo.getView(R.id.navigation_recipes));
        assertTrue(solo.waitForActivity(RecipesMainScreen.class));
        solo.clickOnView(solo.getView(R.id.navigation_meal_plan));
        assertTrue(solo.waitForActivity(MealPlanMainScreen.class));
    }

    @Test
    public void clickOnBackButtonInIngredients(){
        clickOnIngredientsIconInNavBar();
        solo.clickOnView(solo.getView(R.id.top_bar_back_button));
    }

    @Test
    public void clickOnBackButtonInRecipes(){
        clickOnRecipesButtonInNavBar();
        solo.clickOnView(solo.getView(R.id.top_bar_back_button));
    }

    @Test
    public void clickOnIngredientsIconInMainActivity(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.MainActivityIngredientButton));
        solo.assertCurrentActivity("Wrong Activity", IngredientsMainScreen.class);
    }

    @Test
    public void clickOnRecipesIconInMainActivity(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.mainActivityRecipeButton));
        solo.assertCurrentActivity("Wrong Activity", RecipesMainScreen.class);
    }

    @Test
    public void clickOnShoppingCartIconInMainActivity(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.mainActivityShoppingListButton));
        solo.assertCurrentActivity("Wrong Activity", ShoppingCartMainScreen.class);
    }

    @Test
    public void clickOnMealPlanIconInMainActivity(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.mainActivityMealplanButton));
        solo.assertCurrentActivity("Wrong Activity", MealPlanMainScreen.class);
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
