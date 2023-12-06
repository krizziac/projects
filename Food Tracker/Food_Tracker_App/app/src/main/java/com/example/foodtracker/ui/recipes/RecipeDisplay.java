package com.example.foodtracker.ui.recipes;

import static java.lang.String.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.MenuItem;
import com.example.foodtracker.model.recipe.Recipe;
import com.example.foodtracker.ui.NavBar;
import com.example.foodtracker.ui.TopBar;
import com.example.foodtracker.utils.BitmapUtil;

import java.util.Locale;

/**
 * An object of this class is used to represent an activity which is opened when a recipe is clicked
 * This class extends from {@link AppCompatActivity}
 */
public class RecipeDisplay extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> editRecipeActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        if (activityResult.getData() != null && activityResult.getData().getExtras() != null) {
            Recipe receivedRecipe = (Recipe) activityResult.getData().getSerializableExtra("EDIT_RECIPE");
            Intent intent = new Intent(getApplicationContext(), RecipesMainScreen.class);
            intent.putExtra("EDITED_RECIPE", receivedRecipe);
            startActivity(intent);
        }
    });

    //private ArrayAdapter<Ingredient> adapter;
    private RecipeIngredientsRecyclerViewAdapter adapter;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_display);

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("RECIPE");

        RecipeIngredientsRecyclerViewAdapter adapter = new RecipeIngredientsRecyclerViewAdapter(this, recipe.getIngredients());

        TextView recipeTitle = findViewById(R.id.recipeDisplayTitle);
        TextView recipePrepTime = findViewById(R.id.preparation_time);
        TextView recipeServings = findViewById(R.id.servings);
        TextView recipeCategory = findViewById(R.id.category);
        TextView recipeComment = findViewById(R.id.comments);
        RecyclerView recipeIngredients = findViewById(R.id.recipeDisplayIngredientList);
        Button deleteRecipeButton = findViewById(R.id.recipeDeleteButton);
        Button editRecipeButton = findViewById(R.id.recipeEditButton);
        ImageView recipeImage = findViewById(R.id.recipe_display_image);

        recipeTitle.setText(recipe.getTitle());
        recipePrepTime.setText(String.valueOf(recipe.getPrepTime()));
        recipeServings.setText(String.valueOf(recipe.getServings()));
        recipeCategory.setText(recipe.getCategory());

        if (recipe.getComment().isEmpty()) {
            recipeComment.setText(format("\n%s", "No comments"));
            recipeComment.setTextColor(getResources().getColor(R.color.primary_light));
        } else {
            recipeComment.setText(format("\n%s", recipe.getComment()));
        }

        if (!recipe.getImage().isEmpty()) {
            recipeImage.setImageBitmap(BitmapUtil.fromString(recipe.getImage()));
        } else {
            recipeImage.setVisibility(View.GONE);
        }


        recipeIngredients.setLayoutManager(new LinearLayoutManager(this));
        recipeIngredients.setAdapter(adapter);

        if (savedInstanceState == null) {
            createNavbar();
            createTopBar();
        }

        editRecipeButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(getApplicationContext(), RecipeDialog.class);
            editIntent.putExtra("EDIT_RECIPE", recipe);
            editRecipeActivityResultLauncher.launch(editIntent);
        });

        deleteRecipeButton.setOnClickListener(v -> {
            Intent deleteIntent = new Intent();
            deleteIntent.putExtra("DELETED_RECIPE", recipe);
            setResult(RESULT_OK, deleteIntent);
            finish();
        });
    }

    private void createNavbar() {
        NavBar navBar = NavBar.newInstance(MenuItem.RECIPES);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.recipe_display_nav_bar, navBar)
                .commit();
    }

    /**
     * Instantiates the top bar fragment for the recipe display menu
     */
    private void createTopBar() {
        TopBar topBar = TopBar.newInstance("Recipe Details", false, true);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.topBarContainerView, topBar)
                .commit();
    }
}