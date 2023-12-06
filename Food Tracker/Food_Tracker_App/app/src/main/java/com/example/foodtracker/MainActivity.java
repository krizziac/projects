package com.example.foodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodtracker.model.MenuItem;
import com.example.foodtracker.ui.ingredients.IngredientsMainScreen;
import com.example.foodtracker.ui.mealPlan.MealPlanMainScreen;
import com.example.foodtracker.ui.recipes.RecipesMainScreen;
import com.example.foodtracker.ui.shoppingCart.ShoppingCartMainScreen;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        Button ingredientsButton = findViewById(R.id.MainActivityIngredientButton);
        Button recipesButton = findViewById(R.id.mainActivityRecipeButton);
        Button mealPlanButton = findViewById(R.id.mainActivityMealplanButton);
        Button shoppingCartButton = findViewById(R.id.mainActivityShoppingListButton);

        ingredientsButton.setOnClickListener(view -> {
            Intent ingredientsIntent = new Intent(view.getContext(), IngredientsMainScreen.class);
            startActivity(ingredientsIntent);
        });

        recipesButton.setOnClickListener(view -> {
            Intent recipesIntent = new Intent(view.getContext(), RecipesMainScreen.class);
            startActivity(recipesIntent);
        });

        mealPlanButton.setOnClickListener(view -> {
            Intent mealPlanIntent = new Intent(view.getContext(), MealPlanMainScreen.class);
            startActivity(mealPlanIntent);
        });

        shoppingCartButton.setOnClickListener(view -> {
            Intent shoppingCartIntent = new Intent(view.getContext(), ShoppingCartMainScreen.class);
            startActivity(shoppingCartIntent);
        });
    }
}
