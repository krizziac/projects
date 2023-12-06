package com.example.foodtracker.ui.mealPlan;

import static java.lang.String.format;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.MenuItem;
import com.example.foodtracker.model.mealPlan.MealPlanDay;
import com.example.foodtracker.model.recipe.Recipe;
import com.example.foodtracker.ui.NavBar;
import com.example.foodtracker.ui.TopBar;
import com.example.foodtracker.ui.recipes.RecipeIngredientsRecyclerViewAdapter;
import com.example.foodtracker.utils.BitmapUtil;

public class ViewRecipe extends AppCompatActivity implements
        AddRecipeMPDialog.MealPlanRecipeDialogListener{

    private Recipe recipe;
    private int index;
    private MealPlanDay mealPlanDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        Intent intent = getIntent();

        mealPlanDay = (MealPlanDay) intent.getSerializableExtra("meal_plan_for_recipe_edit");
        index = (int) intent.getSerializableExtra("recipe_edit_index");
        recipe = mealPlanDay.getRecipes().get(index);

        RecipeIngredientsRecyclerViewAdapter adapter = new RecipeIngredientsRecyclerViewAdapter(this, recipe.getIngredients());

        TextView recipeTitle = findViewById(R.id.recipeDisplayTitle);
        TextView recipePrepTime = findViewById(R.id.preparation_time);
        TextView recipeServings = findViewById(R.id.servings);
        TextView recipeCategory = findViewById(R.id.category);
        TextView recipeComment = findViewById(R.id.comments);
        RecyclerView recipeIngredients = findViewById(R.id.recipeDisplayIngredientList);
        Button backButton = findViewById(R.id.recipeReturnButton);
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
        /**
         * when edit button is clicked, dialog fragment appear to change the servings
         */
        editRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable("meal_plan_edit_recipe", recipe);

                AddRecipeMPDialog addRecipeMPDialog = new AddRecipeMPDialog();
                addRecipeMPDialog.setArguments(args);
                addRecipeMPDialog.show(getSupportFragmentManager(), "EDIT_MEAL_PLAN_RECIPE");
            }
        });
        /**
         * when back button is clicked, return to the meal plan main screen
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
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

    @Override
    public void onRecipeAdd(Recipe meal_plan_add_recipe) {
        //do nothing
    }

    @Override
    public void onRecipeEdit(Recipe meal_plan_edit_recipe) {
        int edited_servings = meal_plan_edit_recipe.getServings();
        mealPlanDay.getRecipes().get(index).setServings(edited_servings);
        Intent intent1 = new Intent(getApplicationContext(), MealPlanMainScreen.class);
        intent1.putExtra("meal_plan_after_recipe_edit", mealPlanDay);
        startActivity(intent1);

    }
}