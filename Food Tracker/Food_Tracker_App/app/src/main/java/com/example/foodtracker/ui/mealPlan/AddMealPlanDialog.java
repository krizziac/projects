package com.example.foodtracker.ui.mealPlan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodtracker.R;

/**
 * Dialog that asks user if they would like to create a meal plan or to add a single day to the
 * current meal plan.
 */
public class AddMealPlanDialog extends DialogFragment{

    public static final String CREATE_MEAL_PLAN_TAG = "Create_meal_plan";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.meal_plan_selection_dialog, null);
        Button createPlanButton = view.findViewById(R.id.createMealPlan);
        Button addSinglePlanButton = view.findViewById(R.id.addMealIngredient);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        createPlanButton.setOnClickListener(v -> {
            dismiss();
            new createMealPlanDialog().show(getParentFragmentManager(), "Create_meal_plan");
        });

        addSinglePlanButton.setOnClickListener(v -> {
            dismiss();
            new singleMealPlanDialog().show(getParentFragmentManager(),"Create_single_meal_plan");
        });

        return builder
                .setView(view)
                .setTitle("Create your meal plan")
                .setNegativeButton("Cancel", null)
                .create();
    }
}
