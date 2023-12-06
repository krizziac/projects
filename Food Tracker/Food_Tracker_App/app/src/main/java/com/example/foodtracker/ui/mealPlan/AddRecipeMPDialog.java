package com.example.foodtracker.ui.mealPlan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodtracker.R;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.mealPlan.MealPlanDay;
import com.example.foodtracker.model.recipe.Recipe;
import com.example.foodtracker.utils.Collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This dialog is for confirming the number of servings when a recipe is added to a meal plan
 */
public class AddRecipeMPDialog extends DialogFragment {

    private EditText serving;

    private MealPlanRecipeDialogListener listener;

    public interface MealPlanRecipeDialogListener {
        void onRecipeAdd(Recipe meal_plan_add_recipe);
        void onRecipeEdit(Recipe meal_plan_edit_recipe);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (MealPlanRecipeDialogListener) context;
        } catch (ClassCastException classCastException) {
            throw new RuntimeException("Must implement " + MealPlanRecipeDialogListener.class.getSimpleName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.add_recipe_meal_plan_dialog, null);
        serving = view.findViewById(R.id.recipeServings);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        /**
         * when a selected recipe is added to a meal plan
         */
        if (getArguments().get("meal_plan_add_recipe") != null) {
            Bundle selectedBundle = getArguments();
            Recipe received_recipe = (Recipe) selectedBundle.get("meal_plan_add_recipe");

            AlertDialog dialog = builder.setView(view).setTitle("Add Meal plan recipe")
                    .setPositiveButton("Add", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(v -> {
                    if (addClick(received_recipe)) {
                        dialog.dismiss();
                    }
                });
            });

            return dialog;

        }

        /**
         * when a recipe in a meal plan is to change number of servings
         */
        else {
            Bundle selectedBundle = getArguments();
            Recipe received_recipe = (Recipe) selectedBundle.get("meal_plan_edit_recipe");

            serving.setText(String.valueOf(received_recipe.getServings()));

            AlertDialog dialog = builder.setView(view).setTitle(String.format("Adjust servings for %s", received_recipe.getTitle()))
                    .setPositiveButton("Add", null)
                    .setNegativeButton("Cancel", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(v -> {
                    if (editClick(received_recipe)) {
                        dialog.dismiss();
                    }
                });
            });

            return dialog;

        }

    }


    /**
     * When the add button in the dialog is clicked
     * @param recipe the recipe to be added to the meal plan
     * @return {@link Boolean} true if the required fields are valid; false otherwise
     */
    public Boolean addClick(Recipe recipe) {
        Recipe recipeToAdd = new Recipe();

        recipeToAdd.setTitle(recipe.getTitle());
        recipeToAdd.setCategory(recipe.getCategory());
        recipeToAdd.setPrepTime(recipe.getPrepTime());
        recipeToAdd.setIngredients(recipe.getIngredients());
        recipeToAdd.setComment(recipe.getComment());
        recipeToAdd.setServings(recipe.getServings()); //default serving

        if (setFields(recipeToAdd)) {
            listener.onRecipeAdd(recipeToAdd);
        }

        return setFields(recipeToAdd);
    }

    /**
     * For changing the number of servings of a recipe in a meal plan
     * When the edit button in the dialog is clicked
     * @param recipe the recipe to be changed with number of servings
     * @return {@link Boolean} true if the required fields are valid; false otherwise
     */
    public Boolean editClick(Recipe recipe) {

        if (setFields(recipe)) {
            listener.onRecipeEdit(recipe);
        }

        return setFields(recipe);
    }

    /**
     * Checking if the input amount field of the recipe valid
     * @param recipe
     * @return {@link Boolean} valid return true if the amount is int and greater than 0
     */
    public Boolean setFields(Recipe recipe) {
        Boolean valid = true;

        int old_serving = recipe.getServings();

        String amount_str = serving.getText().toString();
        try {
            int amountInt = Integer.parseInt(amount_str);
            if (amountInt <= 0) {
                serving.setError("Invalid amount");
                return false;
            }

            recipe.setServings(amountInt);
            scaleRecipeIngredients(recipe, old_serving);

        } catch (NumberFormatException e) {
            serving.setError("Invalid amount");
            valid = false;
        }

        return valid;

    }

    /**
     * Scaling the amount of ingredients for a recipe when the number of servings of a recipe is changed
     * @param recipe the recipe has the number of servings changed
     * @param old_servings the number of servings of the recipe before changing
     */
    public void scaleRecipeIngredients(Recipe recipe, int old_servings) {
        int new_servings = recipe.getServings();
        double old_double = old_servings;
        double new_double = new_servings;

        double ratio = new_double / old_double;

        double old_ingredient_amount;
        double new_amount_double;

        for (int i=0; i<recipe.getIngredients().size(); i++) {
            old_ingredient_amount = recipe.getIngredients().get(i).getAmountQuantity();
            new_amount_double = ratio * old_ingredient_amount;
            recipe.getIngredients().get(i).setAmountQuantity(new_amount_double);
        }
    }

}
