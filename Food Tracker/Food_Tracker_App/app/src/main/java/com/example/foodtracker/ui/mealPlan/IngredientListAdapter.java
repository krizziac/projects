package com.example.foodtracker.ui.mealPlan;

import static java.lang.String.format;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodtracker.R;
import com.example.foodtracker.model.ingredient.Ingredient;

import java.util.ArrayList;

public class IngredientListAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> ingredientArrayList;
    private Context context;

    public IngredientListAdapter(Context context, ArrayList<Ingredient> ingredientArrayList) {
        super(context, 0, ingredientArrayList);
        this.ingredientArrayList = ingredientArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mp_add_ingredient_content, parent, false);
        }
        Ingredient ingredient = ingredientArrayList.get(position);

        TextView ingredientName = view.findViewById(R.id.ingredient_name);
        TextView ingredientAmount = view.findViewById(R.id.text_ingredient_quantity);
        TextView ingredientLocation = view.findViewById(R.id.text_ingredient_location);
        TextView ingredientCategory = view.findViewById(R.id.text_ingredient_category);
        TextView ingredientDate = view.findViewById(R.id.text_ingredient_expiry);

        ingredientName.setText(ingredient.getDescription());
        ingredientAmount.setText(String.format("Quantity: %s %s",
                ingredient.getAmount(), ingredient.getUnitAbbreviation()));
        ingredientLocation.setText(ingredient.getLocation());
        ingredientCategory.setText(ingredient.getCategory());
        ingredientDate.setText(ingredient.getExpiry());

        return view;
    }
}
