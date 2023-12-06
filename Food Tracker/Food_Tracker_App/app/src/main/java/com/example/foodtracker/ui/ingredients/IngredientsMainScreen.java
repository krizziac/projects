package com.example.foodtracker.ui.ingredients;

import static com.example.foodtracker.ui.ingredients.dialogs.IngredientDialog.INGREDIENT_DIALOG_TAG;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.MenuItem;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.ui.NavBar;
import com.example.foodtracker.ui.Sort;
import com.example.foodtracker.ui.TopBar;
import com.example.foodtracker.ui.ingredients.dialogs.IngredientDialog;
import com.example.foodtracker.utils.Collection;

import java.util.ArrayList;


/**
 * This class creates an object that is used to represent the main screen for the Ingredients
 * This class extends from {@link AppCompatActivity}
 * THis class implements the {@link IngredientDialog.IngredientDialogListener} from {@link IngredientDialog} class
 * Implements {@link com.example.foodtracker.ui.TopBar.TopBarListener} to provide callback on add press
 */
public class IngredientsMainScreen extends AppCompatActivity implements
        IngredientDialog.IngredientDialogListener,
        IngredientRecyclerViewAdapter.IngredientArrayListener,
        TopBar.TopBarListener {

    /**
     * This is a private final variable
     * This holds a collection of {@link Ingredient} objects and is of type {@link Ingredient}
     */
    private final Collection<Ingredient> ingredientsCollection = new Collection<>(Ingredient.class, new Ingredient());
    /**
     * This is a private variable
     * This holds the adapter for the ingredient list
     */
    private final ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    /**
     * This is a private variable
     * This holds a list of {@link Ingredient} objects and is of type {@link ArrayList<Ingredient>}
     */
    private final IngredientRecyclerViewAdapter adapter = new IngredientRecyclerViewAdapter(this, ingredientArrayList);

    /**
     * Allows for sorting of Ingredients by field name
     */
    private Sort<Ingredient.FieldName, IngredientRecyclerViewAdapter, Ingredient> sort;

    /**
     * This is the constructor for the class
     */
    public IngredientsMainScreen() {
        super(R.layout.ingredient_main);
    }

    /**
     * This function is called when a main screen object is created
     *
     * @param savedInstanceState This is of type {@link Bundle}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeSort();
        if (savedInstanceState == null) {
            createRecyclerView();
            createNavbar();
            createTopBar();
        }
    }

    /**
     * This is called when an Ingredient is added by clicking on the add button
     * This is the implementation of a function from {@link IngredientDialog.IngredientDialogListener}
     *
     * @param addedIngredient This is the Ingredient that is added which is of type {@link Ingredient}
     */
    @Override
    public void onIngredientAdd(Ingredient addedIngredient) {
        addIngredient(addedIngredient);
    }

    @Override
    public void onIngredientEdit(Ingredient oldIngredient) {
        editIngredient(oldIngredient);
    }

    @Override
    public void onEdit(Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);

        IngredientDialog editFragment = new IngredientDialog();
        editFragment.setArguments(args);
        editFragment.show(getSupportFragmentManager(), "EDIT_INGREDIENT");
    }

    @Override
    public void onDelete(Ingredient ingredient) {
        removeIngredient(ingredient);
    }

    @Override
    public void onAddClick() {
        new IngredientDialog().show(getSupportFragmentManager(), INGREDIENT_DIALOG_TAG);
    }


    private void createRecyclerView() {
        RecyclerView ingredientsRecyclerView = findViewById(R.id.ingredient_list);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(adapter);
    }

    private void addIngredient(Ingredient ingredient) {
        ingredientArrayList.add(ingredient);
        ingredientsCollection.createDocument(ingredient, () -> {
                    adapter.notifyItemInserted(ingredientArrayList.indexOf(ingredient));
                    sort.sortByFieldName();
                }
        );
    }

    private void editIngredient(Ingredient ingredient) {
        int editIndex = ingredientArrayList.indexOf(ingredient);
        ingredientsCollection.updateDocument(ingredient, () -> adapter.notifyItemChanged(editIndex));
    }

    private void removeIngredient(Ingredient ingredient) {
        int removedIndex = ingredientArrayList.indexOf(ingredient);
        ingredientArrayList.remove(removedIndex);
        ingredientsCollection.delete(ingredient, () ->
                adapter.notifyItemRemoved(removedIndex));
    }

    /**
     * Instantiates the navbar fragment for the ingredients menu
     */
    private void createNavbar() {
        NavBar navBar = NavBar.newInstance(MenuItem.INGREDIENTS);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.navbarContainerView, navBar)
                .commit();
    }

    /**
     * Instantiates the top bar fragment for the ingredients menu
     */
    private void createTopBar() {
        TopBar topBar = TopBar.newInstance("Ingredients", true, false);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.topBarContainerView, topBar)
                .commit();
    }

    private void initializeSort() {
        sort = new Sort<>(this.ingredientsCollection, this.adapter, this.ingredientArrayList, Ingredient.FieldName.class);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.sort_spinner, sort)
                .commit();
    }
}