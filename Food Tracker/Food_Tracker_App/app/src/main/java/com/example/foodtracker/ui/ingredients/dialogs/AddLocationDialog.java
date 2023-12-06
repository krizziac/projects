package com.example.foodtracker.ui.ingredients.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodtracker.R;
import com.example.foodtracker.model.ingredient.Location;
import com.example.foodtracker.utils.Collection;

/**
 * An object of this represents the dialog created to add a new Ingredient location
 * This class extends from {@link DialogFragment}
 */
public class AddLocationDialog extends DialogFragment {

    private final Collection<Location> locationCollection = new Collection<>(Location.class, new Location());
    private final DialogInterface.OnDismissListener dismissListener;
    private EditText listItem;

    public AddLocationDialog(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.singleton_list_add, null);
        listItem = view.findViewById(R.id.singleton_list_add);
        listItem.setHint("Add a location");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.setView(view)
                .setTitle("New Location")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", null).create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> submitLocation());
        });
        return dialog;
    }

    private void submitLocation() {
        Location location = new Location(listItem.getText().toString());
        if (location.getName().isEmpty()) {
            listItem.setError("Location is required!");
            return;
        }
        locationCollection.exists(location, result -> {
            if (Boolean.FALSE.equals(result)) {
                locationCollection.createDocument(location, this::dismiss);
            } else {
                listItem.setError("Location already exists");
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }
}
