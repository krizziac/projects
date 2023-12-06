package com.example.foodtracker.ui_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.foodtracker.R;
import com.example.foodtracker.ui.recipes.RecipeDialog;
import com.example.foodtracker.ui.recipes.RecipeDisplay;
import com.example.foodtracker.ui.recipes.RecipesMainScreen;
import com.robotium.solo.Solo;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.foodtracker.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Class to test the the functionality of Recipes (Viewing, Adding, Editing and Deleting {@link com.example.foodtracker.model.recipe.Recipe}
 * @version 2.0
 */
public class RecipeTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityRule.getActivity());
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_recipes));
        assertTrue(solo.waitForActivity(RecipesMainScreen.class));
    }

    /**
     * Test to see if {@link RecipeDisplay} activity opens on clicking on a recipe in recycler view
     */
    @Test
    public void recipeDisplayOpenOnClick(){
        ArrayList<TextView> textViews = solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", RecipeDisplay.class);
        String clickedRecipeTitle = String.valueOf(textViews.get(0).getText());
        solo.waitForCondition(() -> solo.searchText(clickedRecipeTitle), 2000);
        assertTrue(solo.searchText("Preparation Time"));
        assertTrue(solo.searchText("Servings"));
        assertTrue(solo.searchText("Category"));
        assertTrue(solo.searchText("Ingredients"));
        assertTrue(solo.searchText("Comment"));
    }

    /**
     * Test to check the functionality of the add button in top bar
     */
    @Test
    public void checkTopBarAddButtonFunctionality(){
        solo.clickOnView(solo.getView(R.id.top_bar_add_button));
        solo.assertCurrentActivity("Wrong Activity", RecipeDialog.class);
    }

    /**
     * Test to check functionality of cancel button in {@link RecipeDialog} activity
     */
    @Test
    public void cancelAddNewRecipe(){
        checkTopBarAddButtonFunctionality();
        solo.clickOnView(solo.getView(R.id.recipes_cancel));
        solo.assertCurrentActivity("Wrong Activity", RecipesMainScreen.class);
    }

    /**
     * Test to add a new recipe called PB&J Sandwich and see if it appears in the recycler view list
     */
    @Test
    public void addNewRecipe(){
        checkTopBarAddButtonFunctionality();
        solo.enterText((EditText) solo.getView(R.id.recipe_title), "PB&J Sandwich");
        solo.enterText((EditText) solo.getView(R.id.recipe_prep_time), "2");
        solo.enterText((EditText) solo.getView(R.id.recipe_servings), "1");
        solo.pressSpinnerItem(0,0);

        solo.clickOnView(solo.getView(R.id.addIngredient));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescription), "Bread");
        solo.enterText((EditText) solo.getView(R.id.ingredientQuantity), "2");
        Spinner spinner = solo.getView(Spinner.class, 1);
        spinner.setSelection(0, true);
        Spinner spinner2 = solo.getView(Spinner.class, 2);
        spinner2.setSelection(1, true);
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertTrue(solo.waitForText("Bread"));

        solo.clickOnView(solo.getView(R.id.addIngredient));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescription), "Peanut Butter");
        solo.enterText((EditText) solo.getView(R.id.ingredientQuantity), "2");
        spinner = solo.getView(Spinner.class, 1);
        spinner.setSelection(5, true);
        spinner2 = solo.getView(Spinner.class, 2);
        spinner2.setSelection(1, true);
        solo.clickOnView(solo.getView(android.R.id.button1));

        solo.clickOnView(solo.getView(R.id.addIngredient));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescription), "Strawberry Jam");
        solo.enterText((EditText) solo.getView(R.id.ingredientQuantity), "2");
        spinner = solo.getView(Spinner.class, 1);
        spinner.setSelection(5, true);
        spinner2 = solo.getView(Spinner.class, 2);
        spinner2.setSelection(1, true);
        solo.clickOnView(solo.getView(android.R.id.button1));

        solo.enterText((EditText) solo.getView(R.id.recipeComments), "Use when hungry but too tired to cook");

        solo.clickOnView(solo.getView(R.id.recipes_confirm));
        assertTrue(solo.waitForText("PB&J Sandwich"));
    }

    /**
     * Check the functionality of the cancel button when adding ingredient in  activity
     */
    @Test
    public void checkCancelAddIngredientButton(){
        checkTopBarAddButtonFunctionality();
        solo.clickOnView(solo.getView(R.id.addIngredient));
        solo.clickOnView(solo.getView(android.R.id.button2));
    }

    /**
     * Test to edit a recipe
     */
    @Test
    public void editRecipe(){
        solo.clickOnText("PB&J Sandwich");
        solo.clickOnView(solo.getView(R.id.recipeEditButton));
        solo.clearEditText((EditText) solo.getView(R.id.recipe_title));
        solo.clearEditText((EditText) solo.getView(R.id.recipe_prep_time));
        solo.clearEditText((EditText) solo.getView(R.id.recipe_servings));
        solo.enterText((EditText) solo.getView(R.id.recipe_title), "Peanut Butter and Jelly Sandwiches");
        solo.enterText((EditText) solo.getView(R.id.recipe_prep_time), "5");
        solo.enterText((EditText) solo.getView(R.id.recipe_servings), "2");
        solo.pressSpinnerItem(0,0);
        solo.clickInRecyclerView(0);
        solo.clearEditText((EditText) solo.getView(R.id.ingredientQuantity));
        solo.enterText((EditText) solo.getView(R.id.ingredientQuantity), "500");
        Spinner spinner = solo.getView(Spinner.class, 0);
        spinner.setSelection(1, true);
        solo.clickOnView(solo.getView(android.R.id.button1));
        solo.clickOnView(solo.getView(R.id.recipes_confirm));
        assertTrue(solo.waitForText("Peanut Butter and Jelly Sandwiches"));
    }

    /**
     * Test to delete a recipe and check if it is removed from the recycler view list
     */
    @Test
    public void deleteRecipe(){
        int count = getCount();
        if(count > 0) {
            String clickedRecipeTitle;
            if(solo.searchText("PB&J Sandwich")){
                solo.clickOnText("PB&J Sandwich");
                clickedRecipeTitle = "PB&J Sandwich";
            }
            else if(solo.searchText("Peanut Butter and Jelly Sandwiches")){
                solo.clickOnText("Peanut Butter and Jelly Sandwiches");
                clickedRecipeTitle = "Peanut Butter and Jelly Sandwiches";
            }
            else{
                ArrayList<TextView> textViews = solo.clickInRecyclerView(0);
                solo.assertCurrentActivity("Wrong Activity", RecipeDisplay.class);
                clickedRecipeTitle = String.valueOf(textViews.get(0).getText());
            }
            solo.clickOnView(solo.getView(R.id.recipeDeleteButton));
            final String title = clickedRecipeTitle;
            solo.waitForCondition(() -> !solo.searchText(title), 2000);
        }
    }

    /**
     * Test to check the functionality of the back button in the top bar
     */
    @Test
    public void testTopBarBackButton(){
        solo.clickOnView(solo.getView(R.id.top_bar_back_button));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Function to count and return the number of items in the recipe list recyclerview adapter
     * @return The number of items in recycler view
     */
    public int getCount(){
        solo.sleep(1000);
        RecyclerView view = (RecyclerView) solo.getView(R.id.recipe_list);
        return view.getAdapter().getItemCount();
    }

    /**
     * Function to check if sorting is working
     */
    public void checkSort(){
        int count = getCount();
        if(count > 1) {
            ArrayList<TextView> textView = solo.clickInRecyclerView(0);
            String clickedRecipeTitle = String.valueOf(textView.get(0).getText());
            solo.clickOnView(solo.getView(R.id.top_bar_back_button));

            solo.clickOnView(solo.getView(R.id.sorting_direction));
            solo.sleep(1000);

            ArrayList<TextView> textView1 = solo.clickInRecyclerView(count - 1);
            String clickedRecipeTitle2 = String.valueOf(textView1.get(0).getText());

            assertEquals(clickedRecipeTitle, clickedRecipeTitle2);
        }
    }

    /**
     * Function to check if sorting by title works for ascending and descending
     */
    @Test
    public void checkSortByTitle(){
        solo.pressSpinnerItem(0,0);
        checkSort();
    }

    /**
     * Function to check if sorting by preparation time works for ascending and descending
     */
    @Test
    public void checkSortByPrepTime(){
        solo.pressSpinnerItem(0,1);
        checkSort();
    }

    /**
     * Function to check if sorting by category works for ascending and descending
     */
    @Test
    public void checkSortByCategory(){
        solo.pressSpinnerItem(0, 2);
        checkSort();
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
