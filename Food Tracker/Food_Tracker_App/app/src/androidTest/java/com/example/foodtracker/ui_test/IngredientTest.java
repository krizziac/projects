package com.example.foodtracker.ui_test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.foodtracker.MainActivity;
import com.example.foodtracker.R;
import com.example.foodtracker.ui.ingredients.IngredientsMainScreen;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Class to test the the functionality of Ingredients (Viewing, Adding, Editing and Deleting {@link com.example.foodtracker.model.ingredient.Ingredient}
 * @version 3.0
 */
public class IngredientTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityRule.getActivity());
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_ingredients));
        assertTrue(solo.waitForActivity(IngredientsMainScreen.class));
        solo.waitForText("Category");
    }

    /**
     * Test to check and see if clicking on Item in Recycler View expands it and shows more details
     */
    @Test
    public void checkIngredientListExpandOnClick(){
        solo.clickInRecyclerView(0);
        assertTrue(solo.searchText("Amount"));
        solo.clickInRecyclerView(0);
    }

    /**
     * Test to check if the Edit button works
     * Also checks if the Cancel button works
     */
    @Test
    public void checkEditIngredientAndCancelButtons(){
        solo.clickInRecyclerView(0);
        solo.clickOnView(solo.getView(R.id.edit_ingredient));
        solo.clickOnView(solo.getView(android.R.id.button2));
    }

    /**
     * Test to add a new ingredient called Frozen Buffalo Wings and see if it appears in the list
     */
    @Test
    public void addNewIngredient() {
        solo.waitForText("Category");
        solo.clickOnView(solo.getView(R.id.top_bar_add_button));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescription), " Frozen Buffalo Wings");
        solo.enterText((EditText) solo.getView(R.id.ingredientQuantity), "3");
        Spinner spinner = solo.getView(Spinner.class, 0);
        spinner.setSelection(0, true);
        Spinner spinner2 = solo.getView(Spinner.class, 1);
        spinner2.setSelection(1, true);
        Spinner spinner3 = solo.getView(Spinner.class, 2);
        spinner3.setSelection(4, true);
        solo.setDatePicker(0, 2023, 12, 30);
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertTrue(solo.waitForText("Frozen Buffalo Wings"));
    }

    /**
     * Test to change the first item in the list to Oreo Thins and see if the change is reflected in the list
     */
    @Test
    public void editExistingIngredient(){
        solo.clickInRecyclerView(0);
        solo.clickOnView(solo.getView(R.id.edit_ingredient));
        solo.clearEditText((EditText) solo.getView(R.id.ingredientDescription));
        solo.clearEditText((EditText) solo.getView(R.id.ingredientQuantity));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescription), "Oreo Thins");
        solo.enterText((EditText) solo.getView(R.id.ingredientQuantity), "3");
        Spinner spinner = solo.getView(Spinner.class, 0);
        spinner.setSelection(0, true);
        Spinner spinner2 = solo.getView(Spinner.class, 1);
        spinner2.setSelection(0, true);
        Spinner spinner3 = solo.getView(Spinner.class, 2);
        spinner3.setSelection(0, true);
        solo.setDatePicker(0, 2023, 12, 30);
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertTrue(solo.waitForText("Oreo Thins"));
    }

    /**
     * Test to check if the delete button actually deletes the item
     */
    @Test
    public void clickOnDeleteButton() {
        String clickedItemDescription;
        ArrayList<TextView> textViews = solo.clickInRecyclerView(0);
        clickedItemDescription = String.valueOf(textViews.get(0).getText());
        solo.clickOnView(solo.getView(R.id.delete_ingredient));
        solo.waitForCondition(() -> !solo.searchText(clickedItemDescription), 2000);
    }

    /**
     * Test to check if the plus button in the top bar starts the add ingredient dialog fragment
     */
    @Test
    public void clickOnTopBarAddButton(){
        solo.clickOnView(solo.getView(R.id.top_bar_add_button));
        solo.clickOnView(solo.getView(android.R.id.button2));
    }

    /**
     * Test to click on add new ingredient location and cancel
     */
    @Test
    public void cancelAddIngredientLocation(){
        solo.clickOnView(solo.getView(R.id.top_bar_add_button));
        solo.clickOnView(solo.getView(R.id.add_location));
        assertTrue(solo.waitForText("New Location"));
        solo.enterText((EditText) solo.getView(R.id.singleton_list_add), "Shelf");
        solo.clickOnView(solo.getView(android.R.id.button2));
    }


    /**
     * Test to click on add new ingredient category and then cancel
     */
    @Test
    public void cancelAddIngredientCategory(){
        solo.clickOnView(solo.getView(R.id.top_bar_add_button));
        solo.clickOnView(solo.getView(R.id.add_ingredient_category));
        assertTrue(solo.waitForText("New Category"));
        solo.enterText((EditText) solo.getView(R.id.singleton_list_add), "Frozen");
        solo.clickOnView(solo.getView(android.R.id.button2));
    }

    /**
     * Function to get the total number of items in the ingredient list recycler view
     * @return Number of items in recycler view of type {@link Integer}
     */
    public int getCount(){
        solo.sleep(1000);
        RecyclerView view = (RecyclerView) solo.getView(R.id.ingredient_list);
        return view.getAdapter().getItemCount();
    }

    /**
     * Function to check if the sort button actually works
     */
    public void checkSort(){
        int count = getCount();
        Log.d("Count", Integer.toString(count));
        if(count > 1) {
            ArrayList<TextView> textView = solo.clickInRecyclerView(0);
            String clickedRecipeTitle = String.valueOf(textView.get(0).getText());

            solo.clickOnView(solo.getView(R.id.sorting_direction));
            solo.sleep(1000);

            ArrayList<TextView> textView1 = solo.clickInRecyclerView(0);
            String clickedRecipeTitle2 = String.valueOf(textView1.get(0).getText());
            assertNotEquals(clickedRecipeTitle, clickedRecipeTitle2);
        }
    }

    /**
     * Test to check if sorting by description works for ascending and descending
     */
    @Test
    public void checkSortByDescription(){
        solo.pressSpinnerItem(0,0);
        checkSort();
    }

    /**
     * Test to check if sorting by location works for ascending and descending
     */
    @Test
    public void checkSortByLocation(){
        solo.pressSpinnerItem(0,1);
        checkSort();
    }

    /**
     * Test to check if sorting by category works for ascending and descending
     */
    @Test
    public void checkSortByCategory(){
        solo.pressSpinnerItem(0, 2);
        checkSort();
    }

    /**
     * Test to check if sorting by expiry works for ascending and descending
     */
    @Test
    public void checkSortByExpiry(){
        solo.pressSpinnerItem(0,3);
        checkSort();
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
