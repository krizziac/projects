package com.example.foodtracker.model;

/**
 * Provides callbacks for the edition and deletion of objects
 * @param <T> Represents the type of objects stored in the array
 */
public interface ArrayListener<T> {

    /**
     * Callback on edit request of an item
     * @param object to edit
     */
    void onEdit(T object);

    /**
     * Callback on delete request of an item
     * @param object to delete
     */
    void onDelete(T object);
}
