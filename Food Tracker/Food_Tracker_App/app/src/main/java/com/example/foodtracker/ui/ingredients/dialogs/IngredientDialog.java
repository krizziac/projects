package com.example.foodtracker.ui.ingredients.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodtracker.R;
import com.example.foodtracker.model.IngredientUnit.IngredientUnit;
import com.example.foodtracker.model.ingredient.Category;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.ingredient.Location;
import com.example.foodtracker.model.recipe.SimpleIngredient;
import com.example.foodtracker.utils.Collection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


/**
 * An object of this class represents a dialog used to add a new {@link Ingredient}
 */
public class IngredientDialog extends DialogFragment implements DialogInterface.OnDismissListener {

    public static final String INGREDIENT_DIALOG_TAG = "ADD_INGREDIENT";

    private EditText description;
    private EditText quantity;
    private DatePicker expiry;
    private Ingredient ingredientToEdit;

    private Spinner unit;
    private ArrayAdapter<String> unitAdapter;
    private final List<String> ingredientUnits = new ArrayList<>();

    private Spinner location;
    private ArrayAdapter<String> locationAdapter;
    private final Collection<Location> locationCollection = new Collection<>(Location.class, new Location());
    private final List<String> locations = new ArrayList<>();

    private Spinner category;
    private ArrayAdapter<String> categoryAdapter;
    private final Collection<Category> categoryCollection = new Collection<>(Category.class, new Category());
    private final List<String> categories = new ArrayList<>();


    private IngredientDialogListener listener;

    /**
     * This function is called when the dialog fragment is attached to the current context.
     *
     * @param context This is the context which is of type {@link Context}
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (IngredientDialogListener) context;
        } catch (ClassCastException classCastException) {
            throw new RuntimeException("Must implement " + IngredientDialogListener.class.getSimpleName());
        }
    }

    /**
     * Retrieves locations and categories from firestore and populates a string array with the content,
     * initializes the unit dropdown from {@link com.example.foodtracker.model.IngredientUnit.IngredientUnit} values
     */
    private void refreshDropdowns(@Nullable Ingredient ingredient) {
        categories.clear();
        locations.clear();
        ingredientUnits.clear();
        categoryCollection.getAll(list -> {
            for (Category category : list) {
                categories.add(category.getName().toUpperCase());
                categoryAdapter.notifyDataSetChanged();
            }
            if (ingredient != null) {
                category.setSelection(categoryAdapter.getPosition(ingredient.getCategory()));
            }
        });

        locationCollection.getAll(list -> {
            for (Location location : list) {
                locations.add(location.getName().toUpperCase());
                locationAdapter.notifyDataSetChanged();
            }
            if (ingredient != null) {
                location.setSelection(locationAdapter.getPosition(ingredient.getLocation()));
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
     * This function is called when the dialog fragment is created
     *
     * @param savedInstanceState This is of type {@link Bundle}
     * @return This is of type {@link AlertDialog.Builder}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.add_ingredient_dialog, null);
        description = view.findViewById(R.id.ingredientDescription);
        unit = view.findViewById(R.id.ingredientUnit);
        quantity = view.findViewById(R.id.ingredientQuantity);

        location = view.findViewById(R.id.ingredientLocation);
        locationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, locations);
        location.setAdapter(locationAdapter);

        category = view.findViewById(R.id.ingredientCategory);
        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, categories);
        category.setAdapter(categoryAdapter);

        unit = view.findViewById(R.id.ingredientUnit);
        unitAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, ingredientUnits);
        unit.setAdapter(unitAdapter);

        expiry = view.findViewById(R.id.datePicker);

        Button locationButton = view.findViewById(R.id.add_location);
        locationButton.setOnClickListener(v ->
            new AddLocationDialog(this).show(getParentFragmentManager(), "Add_location"));
        Button categoryButton = view.findViewById(R.id.add_ingredient_category);
        categoryButton.setOnClickListener(v ->
            new AddCategoryDialog(this).show(getParentFragmentManager(), "Add_Category"));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (getArguments() != null) {
            Bundle selectedBundle = getArguments();
            ingredientToEdit = (Ingredient) selectedBundle.get("ingredient");
            return initializeEditIngredientDialog(view, builder);
        }
        return initializeAddIngredientDialog(view, builder);
    }

    @NonNull
    private AlertDialog initializeEditIngredientDialog(View view, AlertDialog.Builder builder) {
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

    @NonNull
    private AlertDialog initializeAddIngredientDialog(View view, AlertDialog.Builder builder) {
        refreshDropdowns(null);
        AlertDialog dialog = builder
                .setView(view)
                .setTitle("Add ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", null).create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                if (addClick()) {
                    dialog.dismiss();
                }
            });
        });
        return dialog;
    }

    public void initializeIngredient(Ingredient ingredient) {
        description.setText(ingredient.getDescription());
        quantity.setText(String.valueOf(ingredient.getAmount()));
        setDatePicker(ingredient);
    }

    public boolean editClick(Ingredient ingredient) {
        if (setIngredientFields(ingredient)) {
            listener.onIngredientEdit(ingredient);
            return true;
        }
        return false;
    }

    public boolean addClick() {
        Ingredient ingredient = new Ingredient();
        if (setIngredientFields(ingredient)) {
            listener.onIngredientAdd(ingredient);
            return true;
        }
        return false;
    }

    private boolean setIngredientFields(Ingredient ingredient) {
        if (description.getText().length() == 0) {
            description.setError("Description is required!");
            return false;
        } else {
            ingredient.setDescription(description.getText().toString());
        }

        if (quantity.getText().length() == 0) {
            quantity.setError("Quantity is required!");
            return false;
        } else {
            double quantity = 1;
            try {
                quantity = Double.parseDouble(this.quantity.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            ingredient.setAmount(quantity);
        }
        ingredient.setUnit(unit.getSelectedItem().toString());
        ingredient.setLocation(location.getSelectedItem().toString());
        ingredient.setCategory(category.getSelectedItem().toString());
        ingredient.setExpiry(String.format(Locale.CANADA, "%02d-%02d-%d",  expiry.getDayOfMonth(),expiry.getMonth() + 1, expiry.getYear()));
        return true;
    }

    private void setDatePicker(Ingredient ingredient) {
        String expiry = ingredient.getExpiry();
        Date expiryDate;
        try {
            expiryDate = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA).parse(expiry);
        } catch (ParseException exception) {
            expiryDate = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.requireNonNull(expiryDate));
        this.expiry.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        refreshDropdowns(ingredientToEdit);
    }

    /**
     * A listener interface which provides callbacks to interact with events occuring in the dialog
     */
    public interface IngredientDialogListener {

        /**
         * Callback when an ingredient is added within the dialog
         */
        void onIngredientAdd(Ingredient newIngredient);

        /**
         * Callback when an ingredient is edited within the dialog
         */
        void onIngredientEdit(Ingredient oldIngredient);

    }
}
