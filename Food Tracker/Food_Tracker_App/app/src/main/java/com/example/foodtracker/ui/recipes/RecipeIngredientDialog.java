package com.example.foodtracker.ui.recipes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodtracker.R;
import com.example.foodtracker.model.IngredientUnit.IngredientUnit;
import com.example.foodtracker.model.ingredient.Category;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.recipe.SimpleIngredient;
import com.example.foodtracker.ui.ingredients.dialogs.AddCategoryDialog;
import com.example.foodtracker.utils.Collection;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the dialog fragment class for adding an ingredient to a recipe
 */
public class RecipeIngredientDialog extends DialogFragment {

    private final Collection<Category> categoryCollection = new Collection<>(Category.class, new Category());
    private final List<String> categories = new ArrayList<>();
    private final List<String> ingredientUnits = new ArrayList<>();
    private EditText description;
    private EditText quantity;
    private Spinner category;
    private ArrayAdapter<String> categoryAdapter;
    private Spinner unit;
    private ArrayAdapter<String> unitAdapter;
    private recipeIngredientDialogListener listener;
    private SimpleIngredient ingredientToEdit;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof recipeIngredientDialogListener) {
            listener = (recipeIngredientDialogListener) context;
        } else {
            throw new RuntimeException("Must implement " + recipeIngredientDialogListener.class.getSimpleName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.smaller_add_ingredient_dialog, null);
        description = view.findViewById(R.id.ingredientDescription);
        quantity = view.findViewById(R.id.ingredientQuantity);

        unit = view.findViewById(R.id.ingredientUnit);
        unitAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, ingredientUnits);
        unit.setAdapter(unitAdapter);

        category = view.findViewById(R.id.ingredientCategory);
        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, categories);
        category.setAdapter(categoryAdapter);

        Button categoryButton = view.findViewById(R.id.smaller_add_ingredient_category);
        categoryButton.setOnClickListener(v ->
            new AddCategoryDialog(this).show(getParentFragmentManager(), "Add_Category"));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.getSerializable("MEAL_PLAN_NEW_INGREDIENT") != null) {
                return createAddMealPlanIngredientDialog(view, builder,arguments);
            }
            return createEditRecipeIngredientDialog(view, builder, arguments);
        }
        return createAddRecipeIngredientDialog(view, builder);
    }

    /**
     * This sets the information of the selected ingredient to the text fields
     *
     * @param ingredient {@link SimpleIngredient} the selected ingredient to be edited
     */
    public void initializeIngredient(SimpleIngredient ingredient) {
        description.setText(ingredient.getDescription());
        quantity.setText(String.valueOf(ingredient.getAmountQuantity()));
    }

    private AlertDialog createAddRecipeIngredientDialog(View view, AlertDialog.Builder builder) {
        refreshDropdowns(null);
        AlertDialog dialog = builder
                .setView(view)
                .setTitle("Add ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", null).create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            SimpleIngredient ingredient = new SimpleIngredient();
            button.setOnClickListener(v -> {
                if (addClick(ingredient)) {
                    dialog.dismiss();
                }
            });
        });
        return dialog;
    }

    private AlertDialog createAddMealPlanIngredientDialog(View view, AlertDialog.Builder builder, Bundle arguments) {
        Ingredient mealPlanIngredient = (Ingredient) arguments.get("MEAL_PLAN_NEW_INGREDIENT");
        refreshDropdowns(null);
        AlertDialog dialog = builder
                .setView(view)
                .setTitle("Add Meal Plan Ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", null).create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                if (mealPlanAddClick(mealPlanIngredient)) {
                    dialog.dismiss();
                }
            });
        });
        return dialog;
    }

    private AlertDialog createEditRecipeIngredientDialog(View view, AlertDialog.Builder builder, Bundle arguments) {
        ingredientToEdit = (SimpleIngredient) arguments.get("selected_ingredient");
        initializeIngredient(ingredientToEdit);
        refreshDropdowns(ingredientToEdit);
        AlertDialog dialog = builder
                .setView(view)
                .setTitle("Edit ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Edit", null).create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                if (editClick(ingredientToEdit)) {
                    dialog.dismiss();
                }
            });
        });
        return dialog;
    }

    /**
     * Retrieves categories from firestore and populates a string array with the content,
     * initializes the unit dropdown from {@link com.example.foodtracker.model.IngredientUnit.IngredientUnit} values
     */
    private void refreshDropdowns(@Nullable SimpleIngredient ingredient) {
        categories.clear();
        ingredientUnits.clear();
        categoryCollection.getAll(list -> {
            for (Category category : list) {
                categories.add(category.getName().toUpperCase());
                categoryAdapter.notifyDataSetChanged();
            }
            if (ingredient != null) {
                category.setSelection(categoryAdapter.getPosition(ingredient.getCategoryName()));
            }
        });

        for (IngredientUnit ingredientUnit : IngredientUnit.values()) {
            ingredientUnits.add(ingredientUnit.name());
        }
        unitAdapter.notifyDataSetChanged();
        if (ingredient != null) {
            unit.setSelection(unitAdapter.getPosition(ingredient.getUnit()));
        }
    }

    /**
     * Set the fields of an ingredient, returns true if the added ingredient is valid
     * and false otherwise
     *
     * @param ingredient {@link SimpleIngredient} the ingredient to be added to a recipe
     * @return true if the added ingredient is valid, false otherwise
     */
    private boolean setFields(SimpleIngredient ingredient) {
        boolean valid = true;

        String addDescription = description.getText().toString();
        ingredient.setDescription(addDescription);
        if (addDescription.isEmpty()) {
            description.setError("Description must not be empty");
            valid = false;
        }

        String addQuantityStr = quantity.getText().toString();
        try {
            double addQuantityInt = Double.parseDouble(addQuantityStr);
            ingredient.setAmountQuantity(addQuantityInt);
        } catch (NumberFormatException e) {
            quantity.setError("Invalid amount");
            valid = false;
        }

        if (unit.getSelectedItem() == null) {
            valid = false;
        } else {
            ingredient.setUnit(unit.getSelectedItem().toString());
        }

        if (category.getSelectedItem() == null) {
            valid = false;
        } else {
            String addCategory = category.getSelectedItem().toString();
            ingredient.setCategoryName(addCategory);
        }

        return valid;
    }

    /**
     * Adds an ingredient to a recipe, returns true if the added ingredient is valid
     * and false otherwise
     *
     * @param ingredient {@link SimpleIngredient} the ingredient to be added to a recipe
     * @return true if the added ingredient is valid, false otherwise
     */
    private boolean addClick(SimpleIngredient ingredient) {
        boolean valid = setFields(ingredient);
        if (valid) {
            listener.addRecipeIngredient(ingredient);
        }
        return valid;
    }

    /**
     * Edits an ingredient in a recipe, returns true if the added ingredient is valid
     * and false otherwise
     *
     * @param ingredient {@link SimpleIngredient} the ingredient to be added to a recipe
     * @return true if the added ingredient is valid, false otherwise
     */
    private boolean editClick(SimpleIngredient ingredient) {
        boolean valid = setFields(ingredient);
        if (valid) {
            listener.editRecipeIngredient(ingredient);
        }
        return valid;
    }

    /**
     * Adds an ingredient to a meal plan, returns true if the added ingredient is valid
     * and false otherwise
     *
     * @param ingredient {@link Ingredient} the ingredient to be added to a meal plan
     * @return true if the added ingredient is valid, false otherwise
     */
    private boolean mealPlanAddClick(Ingredient ingredient) {
        boolean valid = true;

        String addDescription = description.getText().toString();
        ingredient.setDescription(addDescription);
        if (addDescription.isEmpty()) {
            description.setError("Description must not be empty");
            valid = false;
        }

        String addQuantityStr = quantity.getText().toString();
        try {
            double addQuantityDouble = Double.parseDouble(addQuantityStr);
            ingredient.setAmount(addQuantityDouble);
        } catch (NumberFormatException e) {
            quantity.setError("Invalid amount");
            valid = false;
        }

        if (unit.getSelectedItem() == null) {
            valid = false;
        } else {
            ingredient.setUnit(unit.getSelectedItem().toString());
        }

        if (category.getSelectedItem() == null) {
            valid = false;
        } else {
            String addCategory = category.getSelectedItem().toString();
            ingredient.setCategory(addCategory);
        }

        if (valid) {
            listener.addMealPlanIngredient(ingredient);
        }

        return valid;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        refreshDropdowns(ingredientToEdit);
    }

    /**
     * A listener interface which provides callbacks to interact with events occurring in the dialog
     */
    public interface recipeIngredientDialogListener {
        void addRecipeIngredient(SimpleIngredient ingredient);

        void editRecipeIngredient(SimpleIngredient ingredient);

        void addMealPlanIngredient(Ingredient ingredient);
    }
}
