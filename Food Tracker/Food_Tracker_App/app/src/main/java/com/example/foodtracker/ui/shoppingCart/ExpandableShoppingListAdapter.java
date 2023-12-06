package com.example.foodtracker.ui.shoppingCart;

import static java.lang.String.format;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.recipe.SimpleIngredient;
import com.example.foodtracker.model.recipe.SimpleIngredientNameComparator;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ExpandableShoppingListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<String> categories = new ArrayList<>();
    private final Map<String, List<SimpleIngredient>> ingredientsByCategory = new HashMap<>();
    private final ShoppingListListener shoppingListListener;

    public ExpandableShoppingListAdapter(Context context, Set<String> categories, Map<String, Set<SimpleIngredient>> ingredientsByCategory) {
        this.context = context;
        refreshData(categories, ingredientsByCategory);
        shoppingListListener = (ShoppingListListener) context;
    }

    @Override
    public int getGroupCount() {
        return this.categories.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        List<SimpleIngredient> ingredients = this.ingredientsByCategory.get(this.categories.get(listPosition));
        if (ingredients != null) {
            return ingredients.size();
        } else {
            return 0;
        }
    }

    @Override
    public String getGroup(int listPosition) {
        return this.categories.get(listPosition);
    }

    @Override
    public SimpleIngredient getChild(int listPosition, int expandedListPosition) {
        List<SimpleIngredient> ingredients = this.ingredientsByCategory.get(this.categories.get(listPosition));
        if (ingredients != null) {
            return ingredients.get(expandedListPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String category = getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.shopping_cart_content, null);
        }
        TextView title = convertView.findViewById(R.id.shopping_category_name);
        title.setText(category);
        return convertView;
    }

    @Override
    public View getChildView(int position, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        SimpleIngredient ingredient = getChild(position, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.shopping_cart_content_item, null);
        }
        TextView itemName = convertView.findViewById(R.id.shopping_item_name);
        TextView ingredientAmount = convertView.findViewById(R.id.ingredient_amount);
        CheckBox box = convertView.findViewById(R.id.shopping_check_box);
        itemName.setText(ingredient.getDescription());
        ingredientAmount.setText(format(Locale.CANADA, "%.2f x %s", ingredient.getAmountQuantity(), ingredient.getUnitAbbreviation()));
        box.setOnClickListener(onClick -> {
            box.setChecked(false);
            shoppingListListener.onCheck(ingredient);
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void sort(SortingField sortingField, Query.Direction sortingDirection) {
        switch (sortingField) {
            case CATEGORY:
                Collections.sort(this.categories, (categoryA, categoryB) ->
                        Query.Direction.DESCENDING.equals(sortingDirection) ?
                                categoryA.compareToIgnoreCase(categoryB) :
                                categoryB.compareToIgnoreCase(categoryA));
                break;
            case DESCRIPTION:
                for (List<SimpleIngredient> ingredientsInCategory : this.ingredientsByCategory.values()) {
                    Collections.sort(ingredientsInCategory, (ingredientA, ingredientB) -> Query.Direction.DESCENDING.equals(sortingDirection) ?
                            new SimpleIngredientNameComparator().compare(ingredientA, ingredientB) :
                            new SimpleIngredientNameComparator().compare(ingredientB, ingredientA)
                    );
                }
        }
        notifyDataSetChanged();
    }

    public void refreshData(Set<String> categories, Map<String, Set<SimpleIngredient>> ingredientsByCategory) {
        this.ingredientsByCategory.clear();
        this.categories.clear();
        this.categories.addAll(categories);
        for (Map.Entry<String, Set<SimpleIngredient>> ingredientSetByCategory : ingredientsByCategory.entrySet()) {
            List<SimpleIngredient> ingredients = new ArrayList<>(ingredientSetByCategory.getValue());
            this.ingredientsByCategory.put(ingredientSetByCategory.getKey(), ingredients);
        }
        notifyDataSetChanged();
    }

    public interface ShoppingListListener {
        void onCheck(SimpleIngredient ingredient);
    }
}
