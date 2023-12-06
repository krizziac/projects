package com.example.foodtracker.model.recipe;

import com.example.foodtracker.model.Document;
import com.example.foodtracker.model.DocumentableFieldName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recipe extends Document {

    public static final String RECIPES_COLLECTION_NAME = "Recipes-V1.0.0";
    private String image = "";
    private String title;
    private int prepTime = 0;
    private int servings = 0;
    private Category category = new Category();
    private String comment = "";
    private ArrayList<SimpleIngredient> ingredients = new ArrayList<>();

    public Recipe() {
    }



    /**
     * @see <a href=https://www.geeksforgeeks.org/overriding-equals-method-in-java/%22%3EGeeks for Geeks</a>
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        // Check if o is an instance of Recipe or not
        if (!(o instanceof Recipe)) {
            return false;
        }

        // Typecast o to Recipe so that we can compare data members
        Recipe recipe = (Recipe) o;

        return this.getKey().equals(recipe.getKey());
    }

    @Override
    public String getCollectionName() {
        return RECIPES_COLLECTION_NAME;
    }

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put(Recipe.FieldName.TITLE.getName(), this.getTitle());
        data.put(Recipe.FieldName.IMAGE.getName(), this.getImage());
        data.put(Recipe.FieldName.PREPARATION_TIME.getName(), this.getPrepTime());
        data.put(Recipe.FieldName.CATEGORY.getName(), this.getCategory());
        data.put(Recipe.FieldName.SERVINGS.getName(), this.getServings());
        data.put(Recipe.FieldName.COMMENT.getName(), this.getComment());
        data.put(FieldName.INGREDIENTS.getName(), this.getIngredients());
        return data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getCategory() {
        return category.getName();
    }

    public void setCategory(String category) {
        this.category = new Category(category);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<SimpleIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<SimpleIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Represents the field names in the recipes class
     */
    public enum FieldName implements DocumentableFieldName {
        TITLE("title", true),
        IMAGE("image", false),
        PREPARATION_TIME("prepTime", true),
        CATEGORY("category", true),
        SERVINGS("servings", false),
        COMMENT("comment", false),
        INGREDIENTS("ingredients", false);

        private final String name;
        private final boolean sortable;

        FieldName(String fieldName, boolean sortable) {
            this.name = fieldName;
            this.sortable = sortable;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean sortable() {
            return sortable;
        }
    }
}
