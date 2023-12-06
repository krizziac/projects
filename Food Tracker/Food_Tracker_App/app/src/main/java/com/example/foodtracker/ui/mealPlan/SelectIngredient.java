package com.example.foodtracker.ui.mealPlan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.mealPlan.MealPlanDay;
import com.example.foodtracker.model.recipe.SimpleIngredient;
import com.example.foodtracker.ui.TopBar;
import com.example.foodtracker.ui.recipes.RecipeIngredientDialog;
import com.example.foodtracker.utils.Collection;

import java.util.ArrayList;

public class SelectIngredient extends AppCompatActivity implements
        AddIngredientMPDialog.MealPlanIngredientDialogListener,
        TopBar.TopBarListener,
        RecipeIngredientDialog.recipeIngredientDialogListener{

    ListView ingredientListView;
    ArrayList<Ingredient> ingredientArrayList;
    ArrayAdapter<Ingredient> adapter;
    MealPlanDay mealPlan;
    /**
     * This is a private final variable
     * This holds a collection of {@link Ingredient} objects and is of type {@link Ingredient}
     */
    private final Collection<Ingredient> ingredientsCollection = new Collection<>(Ingredient.class, new Ingredient());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_plan_select_ingredient);

        ingredientListView = findViewById(R.id.ingredient_list);
        ingredientArrayList = new ArrayList<>();
        adapter = new IngredientListAdapter(this, ingredientArrayList);
        ingredientListView.setAdapter(adapter);
        getIngredients();

        Intent intent = getIntent();
        mealPlan= (MealPlanDay) intent.getSerializableExtra("meal_plan_for_ingredient_add");

        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient add_ingredient = adapter.getItem(i);

                Bundle args = new Bundle();
                args.putSerializable("meal_plan_add_ingredient", add_ingredient);

                AddIngredientMPDialog addIngredientMPDialog = new AddIngredientMPDialog();
                addIngredientMPDialog.setArguments(args);
                addIngredientMPDialog.show(getSupportFragmentManager(), "ADD_MEAL_PLAN_INGREDIENT");

            }
        });

        if (savedInstanceState == null) {
            createTopBar();
        }
    }

    /**
     * Retrieves ingredients from firestore and populates a string array with the content
     */
    private void getIngredients() {
        ingredientsCollection.getAll(list -> {
            for (Ingredient ingredient : list) {
                ingredientArrayList.add(ingredient);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onIngredientAdd(Ingredient meal_plan_add_ingredient) {
        mealPlan.getIngredients().add(meal_plan_add_ingredient);
        Intent intent1 = new Intent(getApplicationContext(), MealPlanMainScreen.class);
        intent1.putExtra("meal_plan_after_ingredient_add", mealPlan);
        startActivity(intent1);
    }

    @Override
    public void onIngredientEdit(MealPlanDay meal_plan_edit_ingredient) {
        //do nothing
    }

    /**
     * Instantiates the top bar fragment for the recipe display menu
     */
    private void createTopBar() {
        TopBar topBar = TopBar.newInstance("Select Ingredient", true, true);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.topBarContainerView, topBar)
                .commit();
    }

    @Override
    public void onAddClick() {
        /**
         *add a new ingredient which is not in the ingredient storage
         */
        Bundle args = new Bundle();
        Ingredient new_ingredient = new Ingredient();
        args.putSerializable("MEAL_PLAN_NEW_INGREDIENT", new_ingredient);
        RecipeIngredientDialog addNewIngredientMPDialog = new RecipeIngredientDialog();
        addNewIngredientMPDialog.setArguments(args);
        addNewIngredientMPDialog.show(getSupportFragmentManager(), "ADD_NEW_MEAL_PLAN_INGREDIENT");

    }


    @Override
    public void addRecipeIngredient(SimpleIngredient ingredient) {
        //do nothing
    }

    @Override
    public void editRecipeIngredient(SimpleIngredient ingredient) {
        //do nothing
    }

    @Override
    public void addMealPlanIngredient(Ingredient ingredient) {
        mealPlan.getIngredients().add(ingredient);
        Intent intent1 = new Intent(getApplicationContext(), MealPlanMainScreen.class);
        intent1.putExtra("meal_plan_after_ingredient_add", mealPlan);
        startActivity(intent1);
    }
}