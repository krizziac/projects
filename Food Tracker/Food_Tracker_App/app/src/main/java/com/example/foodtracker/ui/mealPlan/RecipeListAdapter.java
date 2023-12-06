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
import com.example.foodtracker.model.recipe.Recipe;

import java.util.ArrayList;

public class RecipeListAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipeArrayList;
    private Context context;

    public RecipeListAdapter(Context context, ArrayList<Recipe> recipeArrayList) {
        super(context, 0, recipeArrayList);
        this.recipeArrayList = recipeArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mp_add_recipe_content, parent, false);
        }

        Recipe recipe = recipeArrayList.get(position);
        TextView recipeTitle = view.findViewById(R.id.recipe_name);
        TextView recipeCategory = view.findViewById(R.id.recipe_category);
        TextView recipeServing = view.findViewById(R.id.recipe_servings);
        TextView recipeTime = view.findViewById(R.id.recipe_preptime);

        recipeTitle.setText(recipe.getTitle());
        recipeCategory.setText(String.format("%s", recipe.getCategory()));
        recipeServing.setText(String.format("Servings: %s", recipe.getServings()));
        recipeTime.setText(String.format("Prep Time: %s mins", recipe.getPrepTime()));

        return view;
    }
}
