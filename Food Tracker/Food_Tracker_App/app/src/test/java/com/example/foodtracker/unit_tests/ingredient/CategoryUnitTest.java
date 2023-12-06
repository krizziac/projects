package com.example.foodtracker.unit_tests.ingredient;

import static org.junit.Assert.assertTrue;

import com.example.foodtracker.model.ingredient.Category;

import org.junit.Test;

import java.util.Map;


public class CategoryUnitTest {
    private Category getMockCategory(){
        Category mockCategory = new Category();
        return mockCategory;
    }
    private Category getMockCategory(String name){
        Category mockCategory = new Category(name);
        return mockCategory;
    }

    @Test
    public void testCategoryConstructors(){
        Category mockCategory1 = getMockCategory();
        Category mockCategory2 = getMockCategory("Fruit");
        assertTrue(mockCategory1.getName() == "");
        assertTrue(mockCategory2.getName() == "Fruit");
    }

    @Test
    public void testGetData(){
        Category mockCategory = getMockCategory("Fruit");
        Map<String,Object> data = mockCategory.getData();
        assertTrue(data.get("name") == "Fruit");
    }

    @Test
    public void testSetAndGetKey(){
        Category mockCategory = getMockCategory();
        mockCategory.setKey("123");
        assertTrue(mockCategory.getKey() == "123");
    }

    @Test
    public void testSetAndGetName(){
        Category mockCategory = getMockCategory();
        mockCategory.setName("Fruit");
        assertTrue(mockCategory.getName() == "Fruit");
    }

    @Test
    public void testHasNonDefaultKey(){
        Category mockCategory = getMockCategory();
        assertTrue(mockCategory.hasNonDefaultKey());
    }

    @Test
    public void testGetCollectionName(){
        Category mockCategory = getMockCategory();
        assertTrue(mockCategory.getCollectionName() == "Ingredients-Category");
    }
}
