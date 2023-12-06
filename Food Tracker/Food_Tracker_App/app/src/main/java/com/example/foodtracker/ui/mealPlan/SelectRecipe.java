package com.example.foodtracker.ui.mealPlan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.mealPlan.MealPlanDay;
import com.example.foodtracker.model.recipe.Recipe;
import com.example.foodtracker.ui.TopBar;
import com.example.foodtracker.utils.Collection;

import java.util.ArrayList;

public class SelectRecipe extends AppCompatActivity implements
        AddRecipeMPDialog.MealPlanRecipeDialogListener{

    ListView recipeListView;
    ArrayList<Recipe> recipeArrayList;
    ArrayAdapter<Recipe> adapter;
    MealPlanDay mealPlan;
    /**
     * This is a private final variable
     * This holds a collection of {@link Recipe} objects and is of type {@link Recipe}
     */
    private final Collection<Recipe> recipesCollection = new Collection<>(Recipe.class, new Recipe());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_plan_select_recipe);

        recipeListView = findViewById(R.id.recipe_list);
        recipeArrayList = new ArrayList<>();
        adapter = new RecipeListAdapter(this, recipeArrayList);
        recipeListView.setAdapter(adapter);
        getRecipes();

        Intent intent = getIntent();
        mealPlan= (MealPlanDay) intent.getSerializableExtra("meal_plan_for_recipe_add");

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe add_recipe = adapter.getItem(i);

                Bundle args = new Bundle();
                args.putSerializable("meal_plan_add_recipe", add_recipe);

                AddRecipeMPDialog addRecipeMPDialog = new AddRecipeMPDialog();
                addRecipeMPDialog.setArguments(args);
                addRecipeMPDialog.show(getSupportFragmentManager(), "ADD_MEAL_PLAN_RECIPE");
            }
        });

        if (savedInstanceState == null) {
            createTopBar();
        }

    }

    /**
     * Retrieves ingredients from firestore and populates a string array with the content
     */
    private void getRecipes() {
        recipesCollection.getAll(list -> {
            for (Recipe recipe : list) {
                recipeArrayList.add(recipe);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRecipeAdd(Recipe meal_plan_add_recipe) {
        mealPlan.getRecipes().add(meal_plan_add_recipe);
        Intent intent1 = new Intent(getApplicationContext(), MealPlanMainScreen.class);
        intent1.putExtra("meal_plan_after_recipe_add", mealPlan);
        startActivity(intent1);
    }

    @Override
    public void onRecipeEdit(Recipe meal_plan_edit_recipe) {
        //do nothing
    }

    /**
     * Instantiates the top bar fragment for the recipe display menu
     */
    private void createTopBar() {
        TopBar topBar = TopBar.newInstance("Select Recipe", false, true);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.topBarContainerView, topBar)
                .commit();
    }
}