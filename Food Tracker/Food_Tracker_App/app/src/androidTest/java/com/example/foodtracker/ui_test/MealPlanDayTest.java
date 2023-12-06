package com.example.foodtracker.ui_test;


import static org.junit.Assert.assertTrue;

import android.widget.EditText;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.foodtracker.MainActivity;
import com.example.foodtracker.R;

import com.example.foodtracker.ui.mealPlan.MealPlanMainScreen;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Class to test the functionality of Meal Plan
 */
public class MealPlanDayTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityRule.getActivity());
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_meal_plan));
        assertTrue(solo.waitForActivity(MealPlanMainScreen.class));
        solo.waitForText("2022");
    }

    @Test
    public void testTopBarAddButtonAndCancelButton(){
        solo.clickOnView(solo.getView(R.id.top_bar_add_button));
        assertTrue(solo.searchText("Create your meal plan"));
        solo.clickOnView(solo.getView(android.R.id.button2));
    }

    @Test
    public void testCreateOrResetMealPlanCancelButton(){
        solo.clickOnView(solo.getView(R.id.top_bar_add_button));
        assertTrue(solo.searchText("Create your meal plan"));
        solo.clickOnView(solo.getView(R.id.createMealPlan));
        solo.clickOnView(solo.getView(android.R.id.button2));
    }

    @Test
    public void testAddAndCancelMealPlanDay(){
        solo.clickOnView(solo.getView(R.id.top_bar_add_button));
        assertTrue(solo.searchText("Create your meal plan"));
        solo.clickOnView(solo.getView(R.id.addMealIngredient));
        solo.clickOnView(solo.getView(android.R.id.button2));
    }



    @Test
    public void testMealPlanDayAddDeleteAndModify(){
        solo.clickOnView(solo.getView(R.id.top_bar_add_button));
        assertTrue(solo.searchText("Create your meal plan"));
        solo.clickOnView(solo.getView(R.id.addMealIngredient));
        solo.setDatePicker(0, 2023, 0, 1);
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertTrue(solo.searchText("01-01-2023"));

        solo.clickOnView(solo.getView(R.id.mealPlanAddIngredientButton));
        solo.clickInList(0);
        solo.enterText((EditText) solo.getView(R.id.ingredientAmount), "3");
        solo.clickOnView(solo.getView(android.R.id.button1));
        solo.clickOnView(solo.getView(R.id.mealPlanAddRecipeButton));
        solo.clickInList(0);
        solo.enterText((EditText) solo.getView(R.id.recipeServings), "3");
        solo.clickOnView(solo.getView(android.R.id.button1));

        solo.clickOnView(solo.getView(R.id.mealPlanDeleteDayButton));
        solo.clickOnView(solo.getView(android.R.id.button2));
        solo.clickOnView(solo.getView(R.id.mealPlanDeleteDayButton));
        solo.clickOnView(solo.getView(android.R.id.button1));
    }

}
