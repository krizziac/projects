package com.example.foodtracker.ui.recipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.ArrayListener;
import com.example.foodtracker.model.recipe.Recipe;

import java.util.ArrayList;

/**
 * This class creates an adapter for the recycler view of ingredients
 * Copyright: COYG
 *
 * @see <a href=https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example">Stack Overflow</a>
 */
public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeHolder> {

    private final ArrayList<Recipe> recipeArrayList;
    private final Context context;
    private final RecyclerViewInterface recyclerViewInterface;

    RecipeRecyclerViewAdapter(Context context, ArrayList<Recipe> recipeArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RecipeRecyclerViewAdapter.RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_content, parent, false);
        return new RecipeHolder(view, recyclerViewInterface);
    }

    /**
     * Populates the view with Recipe information
     */
    @Override
    public void onBindViewHolder(RecipeRecyclerViewAdapter.RecipeHolder holder, int position) {
        Recipe recipe = recipeArrayList.get(position);
        holder.title.setText(recipe.getTitle());
        holder.prepTime.setText(String.format("%s", recipe.getPrepTime()));
        holder.servings.setText(String.format("%s", recipe.getServings()));
        holder.category.setText(recipe.getCategory());
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    /**
     * Represents an {@link Recipe} in our {@link RecipeRecyclerViewAdapter}
     */
    public static class RecipeHolder extends RecyclerView.ViewHolder {

        protected final TextView title = itemView.findViewById(R.id.recipe_name);
        protected final TextView prepTime = itemView.findViewById(R.id.text_recipe_preptime);
        protected final TextView servings = itemView.findViewById(R.id.text_recipe_servings);
        protected final TextView category = itemView.findViewById(R.id.recipe_ingredient_category);

        public RecipeHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}
