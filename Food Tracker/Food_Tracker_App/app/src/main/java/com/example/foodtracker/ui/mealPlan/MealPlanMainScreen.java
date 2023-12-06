package com.example.foodtracker.ui.mealPlan;

import static com.example.foodtracker.ui.mealPlan.AddMealPlanDialog.CREATE_MEAL_PLAN_TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.MenuItem;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.mealPlan.MealPlanDay;
import com.example.foodtracker.model.recipe.Recipe;
import com.example.foodtracker.ui.NavBar;
import com.example.foodtracker.ui.Sort;
import com.example.foodtracker.ui.TopBar;
import com.example.foodtracker.utils.Collection;

import java.util.ArrayList;

/**
 * This class is used to create an object that is used to represent the main screen for Meal Plan
 * This class extends from {@link AppCompatActivity}
 * @version 1.0
 * @see <a href=https://www.geeksforgeeks.org/how-to-create-a-nested-recyclerview-in-android</a>
 */
public class MealPlanMainScreen extends AppCompatActivity implements
        TopBar.TopBarListener,
        MealPlanDayRecyclerViewAdapter.MealPlanDayArrayListener,
        MealPlanDayRecyclerViewAdapter.MealPlanArrayListener,
        createMealPlanDialog.setMPDatesListener,
        singleMealPlanDialog.setSingleMPDatesListener,
        AddIngredientMPDialog.MealPlanIngredientDialogListener
{
    public static final String MEAL_PLAN_AFTER_INGREDIENT_ADD = "meal_plan_after_ingredient_add";
    public static final String MEAL_PLAN_AFTER_RECIPE_ADD = "meal_plan_after_recipe_add";

    private final Collection<MealPlanDay> mealPlanDaysCollection = new Collection<>(MealPlanDay.class, new MealPlanDay());
    private final ArrayList<MealPlanDay> mealPlanDayArrayList = new ArrayList<>();
    private final MealPlanDayRecyclerViewAdapter adapter = new MealPlanDayRecyclerViewAdapter(this, mealPlanDayArrayList);

    private Sort<MealPlanDay.FieldName,MealPlanDayRecyclerViewAdapter,MealPlanDay> sort;


    public MealPlanMainScreen() {
        super(R.layout.meal_plan_main);
    }

    /**
     * @param savedInstanceState this is of type {@link Bundle}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_plan_main);
        initializeSort();

        if (savedInstanceState == null){
            createRecyclerView();
            createNavBar();
            createTopBar();
        }

        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            if (intent.getSerializableExtra(MEAL_PLAN_AFTER_INGREDIENT_ADD) != null) {
                MealPlanDay received_meal_plan = (MealPlanDay) intent.getSerializableExtra(MEAL_PLAN_AFTER_INGREDIENT_ADD);
                addIngredient(received_meal_plan);
            }
            if (intent.getSerializableExtra(MEAL_PLAN_AFTER_RECIPE_ADD) != null) {
                MealPlanDay received_meal_plan = (MealPlanDay) intent.getSerializableExtra(MEAL_PLAN_AFTER_RECIPE_ADD);
                addRecipe(received_meal_plan);
            }
            /**
             * edit recipe
             */
            if (intent.getSerializableExtra("meal_plan_after_recipe_edit") != null) {
                MealPlanDay received_meal_plan = (MealPlanDay) intent.getSerializableExtra("meal_plan_after_recipe_edit");
                editRecipe(received_meal_plan);
            }
        }

    }

    private void createRecyclerView() {
        RecyclerView mealPlanRecyclerView = findViewById(R.id.mealPlanDays);
        mealPlanRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealPlanRecyclerView.setAdapter(adapter);

    }

    private void createNavBar(){
        NavBar navBar = NavBar.newInstance(MenuItem.MEAL_PLANNER);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.meal_plan_nav_bar, navBar)
                .commit();
    }
    private void createTopBar(){
        TopBar topBar = TopBar.newInstance("Meal Plan", true, false);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.topBarContainerView, topBar).commit();
    }


    /**
     * Deletes a single {@link MealPlanDay} object from the meal plan.
     */
    @Override
    public void deleteMealPlan(MealPlanDay mealPlanDay) {
        int removedIndex = mealPlanDayArrayList.indexOf(mealPlanDay);
        mealPlanDayArrayList.remove(removedIndex);

        mealPlanDaysCollection.delete(mealPlanDay, () ->
        adapter.notifyDataSetChanged());
    }

    /**
     * Deletes an ingredient from a meal plan
     * @param ingredientPosition
     * @param mealPlan
     */

    @Override
    public void deleteIngredient(int ingredientPosition, MealPlanDay mealPlan) {
        mealPlanDaysCollection.updateDocument(mealPlan, () -> adapter.notifyDataSetChanged());
    }


    @Override
    public void onAddClick() {
        new AddMealPlanDialog().show(getSupportFragmentManager(), CREATE_MEAL_PLAN_TAG);
    }

    /**
     * deletes a recipe from the meal plan
     * @param recipePosition
     * @param mealPlan
     */
    @Override
    public void deleteRecipe(int recipePosition, MealPlanDay mealPlan) {
        mealPlanDaysCollection.updateDocument(mealPlan, () -> adapter.notifyDataSetChanged());
    }

    /**
     * Creates multiple {@link MealPlanDay} objects all at once, given a list of values.
     * @param listOfDates
     */

    @Override
    public void addMP(ArrayList<String> listOfDates) {

        ArrayList<Ingredient> ingredientArrayList1 = new ArrayList<>();
        ArrayList<Recipe> recipeArrayList1 = new ArrayList<>();


        for (MealPlanDay clearMealPlanDay: mealPlanDayArrayList){
            mealPlanDaysCollection.delete(clearMealPlanDay, () -> {});
        }

        if (!mealPlanDayArrayList.isEmpty()){
            mealPlanDayArrayList.clear();
            adapter.notifyDataSetChanged();
        }

        for (String dates: listOfDates){
            MealPlanDay mealPlanDay = new MealPlanDay(dates, ingredientArrayList1, recipeArrayList1);
            mealPlanDayArrayList.add(mealPlanDay);
            mealPlanDaysCollection.createDocument(mealPlanDay, () ->
                    { adapter.notifyDataSetChanged();
                        sort.sortByFieldName();}
            );

        }
    }

    /**
     * Creates a single {@link MealPlanDay} object.
     * @param day
     */

    @Override
    public void addSingle(String day) {
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();

        MealPlanDay mealPlanDay = new MealPlanDay(day, ingredientArrayList, recipeArrayList);

        mealPlanDayArrayList.add(mealPlanDay);
        mealPlanDaysCollection.createDocument(mealPlanDay, () ->
                {
                    adapter.notifyItemInserted(mealPlanDayArrayList.lastIndexOf(mealPlanDay));
                    sort.sortByFieldName();
                }
        );
    }

    /**
     * Checks if user input is in current meal plan.
     * @param day
     * @return
     */
    @Override
    public boolean isInList(String day) {
        ArrayList<String> mealPlanDays = new ArrayList<>();

        for (MealPlanDay meal: mealPlanDayArrayList){
                mealPlanDays.add(meal.getDay());
        }
        if (mealPlanDays.contains(day)){
            return true;
        }
        return false;
    }

    private void initializeSort() {
        sort = new Sort<>(this.mealPlanDaysCollection, this.adapter, this.mealPlanDayArrayList, MealPlanDay.FieldName.class);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.sort_spinnerMP, sort)
                .commit();
    }


    /**
     * When a ingredient in a meal plan is clicked, change the amount of ingredients
     * @param ingredientPosition the position of the ingredient in a meal plan
     * @param object the meal plan containing the ingredient to change the amount
     */
    @Override
    public void scaleIngredient(int ingredientPosition, MealPlanDay object) {
        Bundle args = new Bundle();
        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();

        bundle1.putSerializable("meal_plan", object);

        //Ingredient ingredient = object.getIngredients().get(ingredientPosition);
        bundle2.putSerializable("ingredient_index", ingredientPosition);

        args.putBundle("meal_plan_edit_ingredient", bundle1);
        args.putBundle("edit_ingredient", bundle2);

        AddIngredientMPDialog addIngredientMPDialog = new AddIngredientMPDialog();
        addIngredientMPDialog.setArguments(args);

        addIngredientMPDialog.show(getSupportFragmentManager(), "EDIT_MEAL_PLAN_INGREDIENT");

    }

    /**
     * When a recipe is clicked to show the recipe details and then change the # of servings
     * @param recipePosition the position of the recipe in the meal plan
     * @param object the meal plan containing the recipe
     */
    @Override
    public void scaleRecipe(int recipePosition, MealPlanDay object) {
        Intent intent = new Intent(getApplicationContext(), ViewRecipe.class);
        intent.putExtra("meal_plan_for_recipe_edit", object);
        intent.putExtra("recipe_edit_index", recipePosition);
        startActivity(intent);
    }


    /**
     * When "add an ingredient" is clicked, the list of ingredients will show up to be selected
     * @param mealPlan where the ingredient is added to
     */
    @Override
    public void onAddIngredientClick(MealPlanDay mealPlan) {
        Intent intent = new Intent(getApplicationContext(), SelectIngredient.class);
        intent.putExtra("meal_plan_for_ingredient_add", mealPlan);
        startActivity(intent);
    }

    /**
     * When add recipe button is clicked, the list of recipes will show up to be selected
     * @param mealPlan where the recipe is added to
     */
    @Override
    public void onAddRecipeClick(MealPlanDay mealPlan) {

        Intent intent = new Intent(getApplicationContext(), SelectRecipe.class);
        intent.putExtra("meal_plan_for_recipe_add", mealPlan);
        startActivity(intent);

    }

    /**
     * When an ingredient is added to a meal plan
     * @param meal_plan_add_ingredient the meal plan with ingredient already added to ingredient arraylist
     */
    public void addIngredient(MealPlanDay meal_plan_add_ingredient) {
        int editIndex = mealPlanDayArrayList.indexOf(meal_plan_add_ingredient);
        mealPlanDaysCollection.updateDocument(meal_plan_add_ingredient, () -> adapter.notifyItemChanged(editIndex));
    }

    /**
     * When a recipe is added to a meal plan
     * @param meal_plan_add_recipe the meal plan with recipe already added to recipe arraylist
     */
    public void addRecipe(MealPlanDay meal_plan_add_recipe) {
        int editIndex = mealPlanDayArrayList.indexOf(meal_plan_add_recipe);
        mealPlanDaysCollection.updateDocument(meal_plan_add_recipe, () -> adapter.notifyItemChanged(editIndex));
    }

    @Override
    public void onIngredientAdd(Ingredient meal_plan_add_ingredient) {
        //do nothing
    }

    /**
     * When the amount of an ingredient in a meal plan is changed
     * @param meal_plan_edit_ingredient the meal plan with changed amount of ingredient
     */
    @Override
    public void onIngredientEdit(MealPlanDay meal_plan_edit_ingredient) {
        int editIndex = mealPlanDayArrayList.indexOf(meal_plan_edit_ingredient);
        mealPlanDaysCollection.updateDocument(meal_plan_edit_ingredient, () -> adapter.notifyItemChanged(editIndex));
    }

    /**
     * When the number of servings in a meal plan is changed
     * @param meal_plan_edit_recipe the meal plan with changed servings of recipe
     */
    public void editRecipe(MealPlanDay meal_plan_edit_recipe) {
        int editIndex = mealPlanDayArrayList.indexOf(meal_plan_edit_recipe);
        mealPlanDaysCollection.updateDocument(meal_plan_edit_recipe, () -> adapter.notifyItemChanged(editIndex));
    }
}