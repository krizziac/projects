package com.example.foodtracker.utils;

import com.example.foodtracker.model.Document;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Collection<T extends Document> {

    private final Class<T> typeParameterClass;
    private final CollectionReference collection;

    public Collection(Class<T> typeParameterClass, T instance) {
        this.typeParameterClass = typeParameterClass;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collection = db.collection(instance.getCollectionName());
    }

    /**
     * Adds all task results to a passed list
     *
     * @param onComplete callback on complete
     */
    public void getAll(ListTask<T> onComplete) {
        collection.get().addOnCompleteListener(getObjectsFromQuery(onComplete));
    }

    /**
     * Adds all task results to a passed list
     *
     * @param onComplete     callback on complete
     * @param sortedByColumn column to sort the results by
     * @param direction      to sort in
     */
    public void getAll(ListTask<T> onComplete, String sortedByColumn, @Nullable Query.Direction direction) {
        Query.Direction sortingDirection = direction != null ? direction : Query.Direction.ASCENDING;
        collection.orderBy(sortedByColumn, sortingDirection).get().addOnCompleteListener(getObjectsFromQuery(onComplete));
    }

    /**
     * Creates a document and sets its document id
     *
     * @param object     document to add to the collection
     * @param onComplete callback when creation is done
     */
    public void createDocument(T object, DocumentTask onComplete) {
        if (object.hasNonDefaultKey()) {
            // we do not want to use the default key from firestore in this case
            updateDocument(object, onComplete);
            return;
        }
        collection.add(object.getData()).addOnSuccessListener(taskResult -> {
            String firestoreId = taskResult.getId();
            object.setKey(firestoreId);
            onComplete.onComplete();
        });
    }

    /**
     * Modifies the document represented by the object, performs onComplete callback when the task completes
     *
     * @param object     edited object to modify in the database
     * @param onComplete callback when edit task is complete
     */
    public void updateDocument(T object, DocumentTask onComplete) {
        if (object.getKey() != null) {
            collection.document(object.getKey()).set(object.getData()).addOnSuccessListener(taskResult -> onComplete.onComplete());
        }
    }

    /**
     * Deletes the document specified by this documents key, with this documents data
     */
    public void delete(T document, DocumentTask onComplete) {
        collection.document(document.getKey()).delete().addOnSuccessListener(taskResult -> onComplete.onComplete());
    }

    /**
     * Checks if a document exists
     */
    public void exists(T document, ResultTask onComplete) {
        collection.document(document.getKey()).get().addOnSuccessListener(taskResult -> {
            if (taskResult.exists()) {
                onComplete.onResult(Boolean.TRUE);
            } else {
                onComplete.onResult(Boolean.FALSE);
            }
        });
    }

    private OnCompleteListener<QuerySnapshot> getObjectsFromQuery(ListTask<T> onTaskComplete) {
        return queryResult -> {
            List<T> list = new ArrayList<>();
            for (DocumentSnapshot document : queryResult.getResult().getDocuments()) {
                T object = document.toObject(typeParameterClass);
                Objects.requireNonNull(object).setKey(document.getId());
                list.add(object);
            }
            onTaskComplete.onComplete(list);
        };
    }

    /**
     * Functional interface providing callback on list task completion
     *
     * @param <T> type of objects contained in the list
     */
    public interface ListTask<T> {

        /**
         * Callback on list task completion with resulting list
         *
         * @param list returned from task
         */
        void onComplete(List<T> list);
    }

    /**
     * Functional interface providing callback on document task completion
     */
    public interface DocumentTask {

        /**
         * Callback on document task completion
         */
        void onComplete();
    }

    /**
     * Functional interface providing callback on document task completion with result
     */
    public interface ResultTask {

        /**
         * Callback on document task completion
         */
        void onResult(Object result);
    }
}
