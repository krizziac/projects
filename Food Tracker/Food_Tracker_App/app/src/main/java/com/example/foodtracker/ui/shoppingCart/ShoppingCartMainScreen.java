package com.example.foodtracker.ui.shoppingCart;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodtracker.R;
import com.example.foodtracker.model.MenuItem;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.mealPlan.MealPlanDay;
import com.example.foodtracker.model.recipe.Recipe;
import com.example.foodtracker.model.recipe.SimpleIngredient;
import com.example.foodtracker.ui.NavBar;
import com.example.foodtracker.ui.TopBar;
import com.example.foodtracker.utils.Collection;
import com.example.foodtracker.utils.ConversionUtil;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is used to create an object that will be used to represent the Shopping Cart Main Screen
 * This class extends from {@link AppCompatActivity}
 */
public class ShoppingCartMainScreen extends AppCompatActivity implements ExpandableShoppingListAdapter.ShoppingListListener {

    private final Collection<Ingredient> ingredientCollection = new Collection<>(Ingredient.class, new Ingredient());
    private final Collection<MealPlanDay> mealPlanDayCollection = new Collection<>(MealPlanDay.class, new MealPlanDay());

    private final Set<SimpleIngredient> ingredientsInStorage = new HashSet<>();
    private final List<MealPlanDay> mealPlanDays = new ArrayList<>();
    private final List<SimpleIngredient> ingredientsInShoppingList = new ArrayList<>();
    private final Set<String> categories = new TreeSet<>();
    private final Map<String, Set<SimpleIngredient>> ingredientsByCategory = new HashMap<>();

    private ExpandableListView shoppingCartExpandableList;
    private ExpandableShoppingListAdapter expandableListAdapter;

    private SortingField sortingFieldName = SortingField.CATEGORY;
    private Query.Direction sortingDirection = Query.Direction.DESCENDING;

    /**
     * @param savedInstanceState This is of type {@link Bundle}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_main);
        shoppingCartExpandableList = findViewById(R.id.shopping_list_expandable);

        refreshAll();

        if (savedInstanceState == null) {
            createNavbar();
            createTopBar();
        }
    }

    @Override
    public void onCheck(SimpleIngredient ingredient) {
        checkoutIngredient(ingredient);
    }

    public void sortShoppingList() {
        for (SortingField sortingField : SortingField.values()) {
            if (sortingField.equals(sortingFieldName)) {
                expandableListAdapter.sort(sortingField, this.sortingDirection);
            }
        }
    }

    private void refreshAll() {
        mealPlanDays.clear();
        ingredientsInStorage.clear();
        mealPlanDayCollection.getAll(mealPlans -> {
            mealPlanDays.addAll(mealPlans);

            ingredientCollection.getAll(ingredients -> {
                List<SimpleIngredient> simpleIngredients = new ArrayList<>();
                for (Ingredient ingredient : ingredients) {
                    simpleIngredients.add(new SimpleIngredient(ingredient));
                }
                ingredientsInStorage.addAll(combineLikewiseIngredients(simpleIngredients));
                refreshShoppingList();
            });
        });
    }

    private void refreshShoppingList() {
        ingredientsInShoppingList.clear();
        ingredientsByCategory.clear();
        categories.clear();
        setIngredientsInShoppingList(getRequiredIngredients());
        setIngredientsByCategory();
        if (expandableListAdapter == null) {
            expandableListAdapter = new ExpandableShoppingListAdapter(this, categories, ingredientsByCategory);
            shoppingCartExpandableList.setAdapter(expandableListAdapter);
            initializeSpinner();
        } else {
            expandableListAdapter.refreshData(categories, ingredientsByCategory);
        }
    }

    private Set<SimpleIngredient> getRequiredIngredients() {
        List<SimpleIngredient> requiredIngredients = new ArrayList<>();
        for (MealPlanDay mealPlanDay : mealPlanDays) {
            for (Ingredient ingredient : mealPlanDay.getIngredients()) {
                requiredIngredients.add(new SimpleIngredient(ingredient));
            }
            for (Recipe recipe : mealPlanDay.getRecipes()) {
                requiredIngredients.addAll(recipe.getIngredients());
            }
        }
        return combineLikewiseIngredients(requiredIngredients);
    }

    private Set<SimpleIngredient> combineLikewiseIngredients(List<SimpleIngredient> ingredients) {
        Set<SimpleIngredient> mergedIngredients = new HashSet<>();
        for (int i = 0; i < ingredients.size(); i++) {
            boolean ingredientAdded = false;
            SimpleIngredient ingredientA = ingredients.get(i);
            for (int j = i + 1; j < ingredients.size(); j++) {
                SimpleIngredient ingredientB = ingredients.get(j);
                if (i != j && ingredientA.equals(ingredientB)) {
                    try {
                        ingredientA.addIngredientAmount(ingredientB.getIngredientAmount());
                        mergedIngredients.add(ingredientA);
                        ingredientAdded = true;
                    } catch (IllegalArgumentException illegalArgumentException) {
                        // do nothing
                    }
                }
            }
            if (!ingredientAdded) {
                mergedIngredients.add(ingredientA);
            }
        }
        return mergedIngredients;
    }

    private void setIngredientsByCategory() {
        for (SimpleIngredient shoppingListIngredient : ingredientsInShoppingList) {
            String shoppingCategory = shoppingListIngredient.getCategory().getName();
            Set<SimpleIngredient> ingredientsInCategory;
            if (ingredientsByCategory.containsKey(shoppingCategory)) {
                ingredientsInCategory = Objects.requireNonNull(ingredientsByCategory.get(shoppingCategory));
                List<SimpleIngredient> categoryWithAddedIngredient = new ArrayList<>(ingredientsInCategory);
                categoryWithAddedIngredient.add(shoppingListIngredient);
                ingredientsInCategory = combineLikewiseIngredients(categoryWithAddedIngredient);
            } else {
                ingredientsInCategory = new HashSet<>();
                ingredientsInCategory.add(shoppingListIngredient);
            }
            ingredientsByCategory.put(shoppingCategory, ingredientsInCategory);
            categories.add(shoppingCategory);
        }
    }

    private void setIngredientsInShoppingList(Set<SimpleIngredient> ingredientsRequired) {
        for (SimpleIngredient requiredIngredient : ingredientsRequired) {
            boolean consideredForShoppingList = false;
            for (SimpleIngredient ingredientInStorage : ingredientsInStorage) {
                if (ingredientInStorage.equals(requiredIngredient)) {
                    SimpleIngredient ingredientToPurchase = new SimpleIngredient();
                    try {
                        ingredientToPurchase.setIngredientAmount(ConversionUtil.getMissingAmount(ingredientInStorage.getIngredientAmount(), requiredIngredient.getIngredientAmount()));
                    } catch (IllegalArgumentException illegalArgumentException) {
                        ingredientToPurchase.setIngredientAmount(requiredIngredient.getIngredientAmount());
                    }
                    ingredientToPurchase.setCategoryName(requiredIngredient.getCategory().getName());
                    ingredientToPurchase.setDescription(requiredIngredient.getDescription());
                    if (ingredientToPurchase.getAmountQuantity() > 0) {
                        ingredientsInShoppingList.add(ingredientToPurchase);

                    }
                    consideredForShoppingList = true;
                }
            }
            if (!consideredForShoppingList) {
                ingredientsInShoppingList.add(requiredIngredient);
            }
        }
    }

    private void checkoutIngredient(SimpleIngredient ingredient) {
        Ingredient newIngredient = new Ingredient(ingredient);
        ingredientCollection.createDocument(newIngredient, this::refreshAll);
    }

    private void initializeSpinner() {
        Spinner spinner = findViewById(R.id.sort_spinner);
        ImageButton sortingDirection = findViewById(R.id.sorting_direction);
        ArrayList<String> fields = new ArrayList<>();
        for (SortingField sortingField : SortingField.values()) {
            fields.add(sortingField.name());
        }
        sortingDirection.setOnClickListener(l -> toggleSortingDirection(sortingDirection));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, fields);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        initializeSelectedItemListener(spinner);
        toggleSortingDirection(sortingDirection);
    }

    private void toggleSortingDirection(ImageButton sortingDirectionButton){
        if (Query.Direction.DESCENDING.equals(this.sortingDirection)) {
            this.sortingDirection = Query.Direction.ASCENDING;
            sortingDirectionButton.setImageResource(R.drawable.ic_baseline_arrow_upward_24);
        } else {
            this.sortingDirection = Query.Direction.DESCENDING;
            sortingDirectionButton.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
        }
        sortShoppingList();
    }

    private void createNavbar() {
        NavBar navBar = NavBar.newInstance(MenuItem.SHOPPING_CART);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.shopping_cart_nav_bar, navBar).commit();
    }

    /**
     * Instantiates the top bar fragment for the ingredients menu
     */
    private void createTopBar() {
        TopBar topBar = TopBar.newInstance("Shopping List", false, false);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.topBarContainerView, topBar).commit();
    }

    private void initializeSelectedItemListener(Spinner sortSpinner) {
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView toSort = view.findViewById(android.R.id.text1);
                sortingFieldName = SortingField.valueOf(toSort.getText().toString());
                sortShoppingList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sortShoppingList();
            }
        });
    }
}