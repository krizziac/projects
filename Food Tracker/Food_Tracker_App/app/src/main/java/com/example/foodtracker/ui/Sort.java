package com.example.foodtracker.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtracker.R;
import com.example.foodtracker.model.Document;
import com.example.foodtracker.model.DocumentableFieldName;
import com.example.foodtracker.utils.Collection;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

/**
 * Allows for the sorting of a collection by document field names
 *
 * @param <E> Enum representing the field names of the document to sort
 * @param <A> The typing of the array list to be used
 * @param <T> Documents that will be sorted
 */
public class Sort<E extends Enum<E> & DocumentableFieldName, A extends RecyclerView.Adapter<?>, T extends Document> extends Fragment {

    private final Collection<T> collection;
    private final List<T> documentList;
    private final A adapter;
    private final EnumSet<E> fieldNameEnum;
    private Query.Direction sortingDirection = Query.Direction.ASCENDING;
    private String sortingFieldName;

    /**
     * Create a sort instance
     *
     * @param collection    of documents to be sorted
     * @param adapter       view that displays the documents to be sorted, allows us to notify the adapter on changes
     * @param list          containing the documents to be sorted
     * @param fieldNameEnum represents the fields that are sortable in the class
     */
    public Sort(Collection<T> collection, A adapter, List<T> list, Class<E> fieldNameEnum) {
        super(R.layout.sort);
        this.collection = collection;
        this.adapter = adapter;
        this.documentList = list;
        this.fieldNameEnum = EnumSet.allOf(fieldNameEnum);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton sortingDirection = view.findViewById(R.id.sorting_direction);
        sortingDirection.setOnClickListener(l -> toggleSortingDirection(sortingDirection));

        List<String> fieldNames = getFieldNames();
        this.sortingFieldName = fieldNames.get(0).toLowerCase(Locale.ROOT);

        Spinner sortSpinner = view.findViewById(R.id.sort_spinner);
        ArrayAdapter<String> sortingAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, fieldNames);
        sortSpinner.setAdapter(sortingAdapter);
        initializeSelectedItemListener(sortSpinner);
    }

    /**
     * Replaces the data in the document list with sorted data from the collection and
     * notifies the adapter of changes
     * Uses {@link this#sortingFieldName} and {@link this#sortingDirection} to determine
     * the field to sort by and the final direction
     */
    public void sortByFieldName() {
        collection.getAll(list -> {
            int oldSize = adapter.getItemCount();
            documentList.clear();
            adapter.notifyItemRangeRemoved(0, oldSize);
            documentList.addAll(list);
            adapter.notifyItemRangeInserted(0, documentList.size());
        }, this.sortingFieldName, this.sortingDirection);
    }

    /**
     * Changes the default sorting direction when selecting a field,
     * will swap the icon on the image button to match.
     * Calls {@link this#sortByFieldName()} to refresh the dataset
     */
    private void toggleSortingDirection(ImageButton sortingDirectionButton) {
        if (Query.Direction.DESCENDING.equals(this.sortingDirection)) {
            this.sortingDirection = Query.Direction.ASCENDING;
            sortingDirectionButton.setImageResource(R.drawable.ic_baseline_arrow_upward_24);
        } else {
            this.sortingDirection = Query.Direction.DESCENDING;
            sortingDirectionButton.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
        }
        this.sortByFieldName();
    }

    private List<String> getFieldNames() {
        List<String> fieldNames = new ArrayList<>();
        for (E fieldName : this.fieldNameEnum) {
            if (fieldName.sortable()) {
                fieldNames.add(fieldName.getName());
            }
        }
        return fieldNames;
    }

    private void initializeSelectedItemListener(Spinner sortSpinner) {
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView toSort = view.findViewById(android.R.id.text1);
                sortingFieldName = toSort.getText().toString();
                sortByFieldName();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sortByFieldName();
            }
        });
    }
}
