package com.example.foodtracker.ui_test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.foodtracker.MainActivity;
import com.example.foodtracker.R;
import com.example.foodtracker.ui.shoppingCart.ShoppingCartMainScreen;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Class to test the functionality of Shopping Cart,
 *
 * @implNote These tests aren't super since the shopping cart is so variable,
 * and it is super difficult to reverse checking off an ingredient by
 * deleting it in our storage since robotium isn't great with recycler views...
 * so these tests will only pass when there are two categories with at least 2
 * items in the first one and will fail otherwise...
 */
public class ShoppingListTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, true);
    private Solo solo;

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityRule.getActivity());
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.navigation_shopping));
        assertTrue(solo.waitForActivity(ShoppingCartMainScreen.class));
        solo.waitForText("CATEGORY");
    }

    @Test
    public void testDropDownExpandOnClick() {
        solo.clickOnView(solo.getView(R.id.shopping_category_name));
        assertTrue(solo.searchText("x"));
    }

    /**
     * Test to see if checking the box removes the ingredient from shopping list and moves it to Ingredients
     */
    @Test
    public void selectIngredientFromList() {
        solo.clickInList(0); // open expand

        // Check first item in shopping list off and see that it is removed
        TextView ingredientNameView = (TextView) solo.getView(R.id.shopping_item_name);
        TextView ingredientUnitView = (TextView) solo.getView(R.id.ingredient_amount);
        solo.clickOnView(solo.getView(R.id.shopping_check_box));
        String ingredientName = ingredientNameView.getText().toString();
        String ingredientUnit = ingredientUnitView.getText().toString().split(" x ")[1];
        solo.sleep(500);
        assertFalse(solo.searchText(ingredientName));
    }

    @Test
    public void testSortByCategory() {
        solo.pressSpinnerItem(0, 0);
        ArrayList<TextView> textView = solo.clickInList(0);
        String clickedRecipeTitle = String.valueOf(textView.get(0).getText());

        solo.clickOnView(solo.getView(R.id.sorting_direction));
        solo.sleep(1000);

        ArrayList<TextView> textView1 = solo.clickInList(0);
        String clickedRecipeTitle2 = String.valueOf(textView1.get(0).getText());
        assertNotEquals(clickedRecipeTitle, clickedRecipeTitle2);
    }

    @Test
    public void testSortByDescription() {
        solo.pressSpinnerItem(0, 1);
        solo.clickInList(0);
    }

}
