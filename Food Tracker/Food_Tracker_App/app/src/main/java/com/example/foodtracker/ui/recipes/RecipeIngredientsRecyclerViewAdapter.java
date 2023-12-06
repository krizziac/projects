package com.example.foodtracker.ui.recipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.ArrayListener;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.recipe.SimpleIngredient;

import java.util.ArrayList;

/**
 * This is a custom adapter for the recycler view for ingredients inside the recipe
 */
public class RecipeIngredientsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeIngredientsRecyclerViewAdapter.RecipeIngredientHolder> {

    /**
     * @implNote For safe type casting
     */
    public interface RecipeIngredientArrayListener extends ArrayListener<SimpleIngredient> {}

    private final ArrayList<SimpleIngredient> ingredientArrayList;
    private final Context context;
    private final boolean editable;

    private RecipeIngredientArrayListener recipeIngredientListener;

    public RecipeIngredientsRecyclerViewAdapter(Context context, ArrayList<SimpleIngredient> ingredientArrayList) {
        this(context, ingredientArrayList, false);
    }

    RecipeIngredientsRecyclerViewAdapter(Context context, ArrayList<SimpleIngredient> ingredientArrayList, boolean editable) {
        this.context = context;
        this.ingredientArrayList = ingredientArrayList;
        this.editable = editable;
        if (editable) {
            this.recipeIngredientListener = (RecipeIngredientArrayListener) context;
        }
    }

    @NonNull
    @Override
    public RecipeIngredientsRecyclerViewAdapter.RecipeIngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_content, parent, false);
        return new RecipeIngredientHolder(view);
    }

    /**
     * Populates the view with Recipe Ingredient information
     */
    @Override
    public void onBindViewHolder(RecipeIngredientsRecyclerViewAdapter.RecipeIngredientHolder ingredientHolder, int position) {
        SimpleIngredient ingredient = ingredientArrayList.get(position);
        ingredientHolder.description.setText(ingredient.getDescription());
        ingredientHolder.amount.setText(String.format("%s", ingredient.getAmountQuantity()));
        ingredientHolder.unit.setText(ingredient.getUnitAbbreviation());
        ingredientHolder.category.setText(String.format("%s", ingredient.getCategoryName()));
    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size();
    }

    /**
     * Represents an {@link Ingredient} in our {@link RecipeIngredientsRecyclerViewAdapter}
     */
    public class RecipeIngredientHolder extends RecyclerView.ViewHolder {

        protected final TextView description = itemView.findViewById(R.id.recipe_ingredient_name);
        protected final TextView amount = itemView.findViewById(R.id.recipe_ingredient_amount);
        protected final TextView unit = itemView.findViewById(R.id.recipe_ingredient_unit);
        protected final TextView category = itemView.findViewById(R.id.recipe_ingredient_category);
        protected final Button deleteButton = itemView.findViewById(R.id.delete_button);
        protected final LinearLayout row = itemView.findViewById(R.id.recipe_ingredient_row);

        public RecipeIngredientHolder(View itemView) {
            super(itemView);
            if (!editable) {
                deleteButton.setVisibility(View.GONE);
            } else {
                deleteButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f));
                row.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.8f));
                row.findViewById(R.id.recipe_ingredient_name).setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.6f));
                row.findViewById(R.id.recipe_ingredient_amount).setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f));
                row.findViewById(R.id.recipe_ingredient_unit).setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,  0.9f));
                row.findViewById(R.id.recipe_ingredient_category).setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.9f));
                deleteButton.setOnClickListener(listener -> {
                    SimpleIngredient ingredientToDelete = ingredientArrayList.get(getAdapterPosition());
                    recipeIngredientListener.onDelete(ingredientToDelete);
                });
                row.setOnClickListener(listener -> {
                    SimpleIngredient ingredientToEdit = ingredientArrayList.get(getAdapterPosition());
                    recipeIngredientListener.onEdit(ingredientToEdit);
                });
            }
        }
    }
}
