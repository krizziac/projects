package com.example.foodtracker.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodtracker.R;
import com.example.foodtracker.model.MenuItem;
import com.example.foodtracker.ui.ingredients.IngredientsMainScreen;
import com.example.foodtracker.ui.mealPlan.MealPlanMainScreen;
import com.example.foodtracker.ui.recipes.RecipesMainScreen;
import com.example.foodtracker.ui.shoppingCart.ShoppingCartMainScreen;

import java.util.HashMap;
import java.util.Map;


/**
 * A navbar {@link Fragment} subclass that controls navigation between different activities
 */
public class NavBar extends Fragment {

    public static final String MENU_HIGHLIGHT = "MENU_HIGHLIGHT";
    private final Map<MenuItem, Integer> menuItemToIconMap = initializeNavbarMap();
    private MenuItem selectedItem;
    private View navbarView;

    public NavBar() {
        super(R.layout.fragment_nav_bar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navbarView = view;
        initializeNavbarMap();
        initializeNavigationListener();
        if (savedInstanceState == null && getArguments() != null) {
            selectedItem = MenuItem.valueOf(getArguments().getString(MENU_HIGHLIGHT));
            setSelectedItem(selectedItem);
        }
    }

    /**
     * Create a navbar with the given selected highlighted item
     *
     * @param highlightedItem Represents the item to highlight in the navbar
     * @return A new instance of fragment NavBar.
     */
    public static NavBar newInstance(MenuItem highlightedItem) {
        NavBar fragment = new NavBar();
        Bundle args = new Bundle();
        args.putString(MENU_HIGHLIGHT, highlightedItem.toString());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * r
     * Based on the passed Menu Item, sets the highlighted icon
     */
    private void setSelectedItem(MenuItem selectedItem) {
        if (selectedItem != null && menuItemToIconMap.containsKey(selectedItem) && navbarView != null) {
            ImageButton icon = navbarView.findViewById(menuItemToIconMap.get(selectedItem));
            icon.setColorFilter(R.color.white, PorterDuff.Mode.DST_IN);
        }
    }

    private void initializeNavigationListener() {
        if (navbarView != null) {
            ImageButton ingredientsButton = navbarView.findViewById(R.id.navigation_ingredients);
            ImageButton recipesButton = navbarView.findViewById(R.id.navigation_recipes);
            ImageButton mealPlanButton = navbarView.findViewById(R.id.navigation_meal_plan);
            ImageButton shoppingCartButton = navbarView.findViewById(R.id.navigation_shopping);

            ingredientsButton.setOnClickListener(view -> {
                if (!MenuItem.INGREDIENTS.equals(selectedItem)) {
                    Intent ingredientsIntent = new Intent(view.getContext(), IngredientsMainScreen.class);
                    startActivity(ingredientsIntent);
                }
            });

            recipesButton.setOnClickListener(view -> {
                if (!MenuItem.RECIPES.equals(selectedItem)) {
                    Intent recipesIntent = new Intent(view.getContext(), RecipesMainScreen.class);
                    startActivity(recipesIntent);
                }
            });

            mealPlanButton.setOnClickListener(view -> {
                if (!MenuItem.MEAL_PLANNER.equals(selectedItem)) {
                    Intent mealPlanIntent = new Intent(view.getContext(), MealPlanMainScreen.class);
                    startActivity(mealPlanIntent);
                }
            });

            shoppingCartButton.setOnClickListener(view -> {
                if (!MenuItem.SHOPPING_CART.equals(selectedItem)) {
                    Intent shoppingCartIntent = new Intent(view.getContext(), ShoppingCartMainScreen.class);
                    startActivity(shoppingCartIntent);
                }
            });
        }
    }

    /**
     * Populates the icon map representing each menu item and its corresponding icon
     */
    private Map<MenuItem, Integer> initializeNavbarMap() {
        Map<MenuItem, Integer> navbarMap = new HashMap<>();
        navbarMap.put(MenuItem.INGREDIENTS, R.id.navigation_ingredients);
        navbarMap.put(MenuItem.RECIPES, R.id.navigation_recipes);
        navbarMap.put(MenuItem.MEAL_PLANNER, R.id.navigation_meal_plan);
        navbarMap.put(MenuItem.SHOPPING_CART, R.id.navigation_shopping);
        return navbarMap;
    }
}