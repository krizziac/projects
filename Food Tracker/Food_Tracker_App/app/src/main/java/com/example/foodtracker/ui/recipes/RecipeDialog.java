package com.example.foodtracker.ui.recipes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.ingredient.Ingredient;
import com.example.foodtracker.model.recipe.Category;
import com.example.foodtracker.model.recipe.Recipe;
import com.example.foodtracker.model.recipe.SimpleIngredient;
import com.example.foodtracker.ui.TopBar;
import com.example.foodtracker.utils.BitmapUtil;
import com.example.foodtracker.utils.Collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDialog extends AppCompatActivity implements
        RecipeIngredientDialog.recipeIngredientDialogListener,
        RecipeIngredientsRecyclerViewAdapter.RecipeIngredientArrayListener {

    public static final String RECIPE_KEY = "recipe";
    private final Collection<Category> categoryCollection = new Collection<>(Category.class, new Category());
    private final List<String> categories = new ArrayList<>();
    private ImageView recipeImage;
    private Bitmap bitmap;

    /**
     * Allows us to launch and handle the result of choosing a picture from the gallery
     * sets the image in the display based on the chosen picture
     */
    private final ActivityResultLauncher<String> imageGalleryResultHandler =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    recipeImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

    /**
     * Allows us to launch and handle the result for taking a picture
     */
    private final ActivityResultLauncher<Void> takePictureResultHandler =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                this.bitmap = bitmap;
                recipeImage.setImageBitmap(this.bitmap);
            });

    private ArrayAdapter<String> categoryAdapter;
    private Spinner categoryField;

    private EditText titleField;
    private EditText timeField;
    private EditText servingsField;
    private EditText commentsField;

    private RecipeIngredientsRecyclerViewAdapter adapter;
    private ArrayList<SimpleIngredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_recipe);

        titleField = findViewById(R.id.recipe_title);
        timeField = findViewById(R.id.recipe_prep_time);
        servingsField = findViewById(R.id.recipe_servings);
        getCategories(null);
        commentsField = findViewById(R.id.recipeComments);
        recipeImage = findViewById(R.id.recipe_image);

        ImageButton addRecipeImageFromGalleryButton = findViewById(R.id.recipe_add_image_from_gallery);
        addRecipeImageFromGalleryButton.setOnClickListener(v -> addImageFromGallery());

        ImageButton addRecipeFromCameraButton = findViewById(R.id.recipe_add_image_from_camera);
        addRecipeFromCameraButton.setOnClickListener(v -> addImageFromCamera());

        ImageButton deleteRecipeImageButton = findViewById(R.id.recipe_remove_image);
        deleteRecipeImageButton.setOnClickListener(v -> {
            bitmap = null;
            recipeImage.setImageBitmap(null);
        });

        RecyclerView recipeIngredients = findViewById(R.id.recipe_ingredients);
        recipeIngredients.setLayoutManager(new LinearLayoutManager(this));
        ingredientList = new ArrayList<>();
        adapter = new RecipeIngredientsRecyclerViewAdapter(this, ingredientList, true);
        recipeIngredients.setAdapter(adapter);

        Button addIngredientButton = findViewById(R.id.addIngredient);
        addIngredientButton.setOnClickListener(view -> new RecipeIngredientDialog().show(getSupportFragmentManager(), "Add_ingredient"));

        Button confirmButton = findViewById(R.id.recipes_confirm);
        if (getIntent().getExtras() != null) {
            createTopBar(getResources().getString(R.string.edit_recipe));
            Recipe recipe = (Recipe) getIntent().getSerializableExtra("EDIT_RECIPE");
            getCategories(recipe);
            initializeEditRecipe(recipe);
            confirmButton.setOnClickListener(view -> {
                Intent intent = new Intent();
                boolean valid = setRecipeFields(recipe);
                if (valid) {
                    intent.putExtra("EDIT_RECIPE", recipe);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        } else {
            recipeImage.setBackgroundColor(getResources().getColor(R.color.background_dark));
            createTopBar(getResources().getString(R.string.add_recipe));
            confirmButton.setOnClickListener(view -> {
                Intent intent = new Intent();
                Recipe recipe = new Recipe();
                getCategories(null);
                boolean valid = setRecipeFields(recipe);
                if (valid) {
                    intent.putExtra(RECIPE_KEY, recipe);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            });
        }

        Button cancelButton = findViewById(R.id.recipes_cancel);
        cancelButton.setOnClickListener(view -> finish());
    }

    @Override
    public void addRecipeIngredient(SimpleIngredient ingredient) {
        ingredientList.add(ingredient);
        adapter.notifyItemChanged(ingredientList.size());
        if (ingredientList.size() == 1) {
            toggleRecipeIngredientsDisplay();
        }
    }

    @Override
    public void editRecipeIngredient(SimpleIngredient ingredient) {
        adapter.notifyItemChanged(ingredientList.indexOf(ingredient));
    }

    @Override
    public void addMealPlanIngredient(Ingredient ingredient) {
        //do nothing
    }

    @Override
    public void onEdit(SimpleIngredient ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("selected_ingredient", ingredient);
        RecipeIngredientDialog ingredientDialog = new RecipeIngredientDialog();
        ingredientDialog.setArguments(args);
        ingredientDialog.show(getSupportFragmentManager(), "EDIT_INGREDIENT_IN_RECIPE");
    }

    @Override
    public void onDelete(SimpleIngredient ingredient) {
        int index = ingredientList.indexOf(ingredient);
        ingredientList.remove(ingredient);
        adapter.notifyItemRemoved(index);
        if (ingredientList.isEmpty()) {
            toggleRecipeIngredientsDisplay();
        }
    }

    public void setImage(Recipe recipe) {
        if (!recipe.getImage().isEmpty()) {
            recipeImage.setImageBitmap(BitmapUtil.fromString(recipe.getImage()));
        }
    }

    /**
     * Populates the dialog fields from a {@link Recipe} instance
     *
     * @param recipe to initialize form with
     */
    public void initializeEditRecipe(Recipe recipe) {
        setImage(recipe);
        titleField.setText(recipe.getTitle());
        timeField.setText(String.valueOf(recipe.getPrepTime()));
        servingsField.setText(String.valueOf(recipe.getServings()));
        commentsField.setText(recipe.getComment());
        for (SimpleIngredient ingredient : recipe.getIngredients()) {
            addRecipeIngredient(ingredient);
        }
    }

    /**
     * Either shows the header or a no ingredients message
     */
    private void toggleRecipeIngredientsDisplay() {
        TextView noIngredientsMessage = findViewById(R.id.no_ingredients);
        LinearLayout ingredientHeaders = findViewById(R.id.ingredientHeaders);
        if (View.VISIBLE == ingredientHeaders.getVisibility()) {
            noIngredientsMessage.setVisibility(View.VISIBLE);
            ingredientHeaders.setVisibility(View.GONE);
        } else {
            noIngredientsMessage.setVisibility(View.GONE);
            ingredientHeaders.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Retrieves categories from firestore and populates a string array with the content
     */
    private void getCategories(@Nullable Recipe recipe) {
        categoryField = findViewById(R.id.recipe_category);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        categoryField.setAdapter(categoryAdapter);
        categoryCollection.getAll(list -> {
            for (Category category : list) {
                categories.add(category.getName().toUpperCase());
                categoryAdapter.notifyDataSetChanged();
            }
            if (recipe != null) {
                categoryField.setSelection(categoryAdapter.getPosition(recipe.getCategory()));
            }
        });
    }

    private void addImageFromGallery() {
        imageGalleryResultHandler.launch("image/*");
    }

    private void addImageFromCamera() {
        takePictureResultHandler.launch(null);
    }

    /**
     * Setting the details of a recipe.
     * Return true if the title is not empty; otherwise, return false;
     */
    private boolean setRecipeFields(Recipe recipe) {
        boolean valid = true;

        String title = titleField.getText().toString();
        recipe.setTitle(title);
        if (title.isEmpty()) {
            titleField.setError("Title must not be empty");
            valid = false;
        }

        String time = timeField.getText().toString();
        int timeMin = 0;
        try {
            timeMin = Integer.parseInt(time);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String servings = servingsField.getText().toString();
        int servingsInt = 0;
        try {
            servingsInt = Integer.parseInt(servings);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String category = categoryField.getSelectedItem().toString();
        String comments = commentsField.getText().toString();

        recipe.setPrepTime(timeMin);
        recipe.setServings(servingsInt);
        recipe.setCategory(category);
        recipe.setComment(comments);
        recipe.setIngredients(ingredientList);
        if (bitmap != null) {
            recipe.setImage(BitmapUtil.toString(bitmap));
        }
        return valid;
    }

    /**
     * Instantiates the top bar fragment for the recipe display menu
     */
    private void createTopBar(String title) {
        TopBar topBar = TopBar.newInstance(title, false, true);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.topBarContainerView, topBar)
                .commit();
    }
}