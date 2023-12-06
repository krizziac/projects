package com.example.foodtracker.unit_tests.recipe;

import static org.junit.Assert.assertTrue;

import com.example.foodtracker.model.recipe.Category;

import org.junit.Test;

import java.util.Map;

public class CategoryUnitTest {
    public Category getMockCategory(){
        Category category = new Category();
        return category;
    }

    public Category getMockCategory(String name){
        Category category = new Category(name);
        return category;
    }

    @Test
    public void testConstructor(){
        Category category = getMockCategory("Snack");
        assertTrue(category.getName() == "Snack");
    }

    @Test
    public void testSetAndGetName(){
        Category category = getMockCategory();
        category.setName("Snack");
        assertTrue(category.getName() == "Snack");
    }

    @Test
    public void testSetAndGetKey(){
        Category category = getMockCategory();
        category.setKey("123");
        assertTrue(category.getKey() == "123");
    }

    @Test
    public void testGetData(){
        Category category = getMockCategory("Snack");
        Map<String, Object> data = category.getData();
        assertTrue(data.get("name") == "Snack");
    }

    @Test
    public void testHasNonDefaultKey(){
        Category category = getMockCategory();
        assertTrue(category.hasNonDefaultKey());
    }

    @Test
    public void testGetCollectionsName(){
        Category category = getMockCategory();
        assertTrue(category.getCollectionName() == "Recipes-Category");
    }
}
