package com.example.foodtracker.ui.recipes;

import static com.example.foodtracker.ui.recipes.RecipeDialog.RECIPE_KEY;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.MenuItem;
import com.example.foodtracker.model.recipe.Recipe;
import com.example.foodtracker.ui.NavBar;
import com.example.foodtracker.ui.Sort;
import com.example.foodtracker.ui.TopBar;
import com.example.foodtracker.utils.Collection;

import java.util.ArrayList;

/**
 * This class creates an object that is used to represent the main screen for the Recipes
 * This class extends {@link AppCompatActivity}
 */
public class RecipesMainScreen extends AppCompatActivity implements
        RecyclerViewInterface,
        TopBar.TopBarListener {


    private final Collection<Recipe> recipesCollection = new Collection<>(Recipe.class, new Recipe());
    private final ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    private final RecipeRecyclerViewAdapter adapter = new RecipeRecyclerViewAdapter(this, recipeArrayList, this);
    private final ActivityResultLauncher<Intent> recipeDisplayResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        if (activityResult.getData() != null && activityResult.getData().getExtras() != null) {
            Recipe recipeToDelete = (Recipe) activityResult.getData().getSerializableExtra("DELETED_RECIPE");
            deleteRecipe(recipeToDelete);
        }
    });

    /**
     * Allows us to sort by a selected field name and refresh the data in the view
     */
    private Sort<Recipe.FieldName, RecipeRecyclerViewAdapter, Recipe> sort;

    private final ActivityResultLauncher<Intent> recipeActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        if (activityResult.getData() != null && activityResult.getData().getExtras() != null) {
            Recipe receivedRecipe = (Recipe) activityResult.getData().getSerializableExtra(RECIPE_KEY);
            addRecipe(receivedRecipe);
        }
    });

    public RecipesMainScreen() {
        super(R.layout.recipes_main);
    }

    /**
     * @param savedInstanceState This is of type {@link Bundle}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_main);
        initializeSort();
        if (savedInstanceState == null) {
            createRecyclerView();
            createNavbar();
            createTopBar();
        }

        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            Recipe received_recipe = (Recipe) intent.getSerializableExtra("EDITED_RECIPE");
            editRecipe(received_recipe);
        }
    }

    /**
     * Called when add button is clicked
     * Starts a new activity {@link RecipeDialog} to add a new recipe
     */
    @Override
    public void onAddClick() {
        Intent intent = new Intent(getApplicationContext(), RecipeDialog.class);
        recipeActivityResultLauncher.launch(intent);
    }

    /**
     * Called When an item in recylerview is clicked
     * Starts a new {@link RecipeDisplay} activity that displays recipe details
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), RecipeDisplay.class);
        Recipe recipe = recipeArrayList.get(position);
        intent.putExtra("RECIPE", recipe);
        recipeDisplayResultLauncher.launch(intent);
    }

    /**
     * Editing recipe in Firebase
     * @param recipe The edited {@link Recipe}
     */
    public void editRecipe(Recipe recipe) {
        int editIndex = recipeArrayList.indexOf(recipe);
        recipesCollection.updateDocument(recipe, () -> adapter.notifyItemChanged(editIndex));
    }

    /**
     * Deleting recipe in Firebase
     * @param recipe The {@link Recipe} to be deleted
     */
    public void deleteRecipe(Recipe recipe) {
        int removedIndex = recipeArrayList.indexOf(recipe);
        recipeArrayList.remove(removedIndex);
        recipesCollection.delete(recipe, () ->
                adapter.notifyItemRemoved(removedIndex));

    }

    /**
     * Adding recipe to Firebase
     * @param recipe The {@link Recipe} to be added
     */
    private void addRecipe(Recipe recipe) {
        recipeArrayList.add(recipe);
        recipesCollection.createDocument(recipe, () -> {
            adapter.notifyItemInserted(recipeArrayList.indexOf(recipe));
            sort.sortByFieldName();
        });
    }

    private void createRecyclerView() {
        RecyclerView recipeRecyclerView = findViewById(R.id.recipe_list);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeRecyclerView.setAdapter(adapter);
    }

    /**
     * Instantiates Navigaton bar fragment for the Recipe Menu
     */
    private void createNavbar() {
        NavBar navBar = NavBar.newInstance(MenuItem.RECIPES);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.recipes_nav_bar, navBar).commit();
    }

    /**
     * Instantiates the top bar fragment for the Recipe menu
     */
    private void createTopBar() {
        TopBar topBar = TopBar.newInstance("Recipes", true, false);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.topBarContainerView, topBar)
                .commit();
    }

    /**
     * Instantiates sorting bar fragment for the Recipe Menu
     */
    private void initializeSort() {
        sort = new Sort<>(this.recipesCollection, this.adapter, this.recipeArrayList, Recipe.FieldName.class);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.sort_spinnerRecipe, sort).commit();
    }
}