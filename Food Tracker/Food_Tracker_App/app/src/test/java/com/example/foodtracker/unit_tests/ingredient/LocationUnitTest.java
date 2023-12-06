package com.example.foodtracker.unit_tests.ingredient;

import static org.junit.Assert.assertTrue;

import com.example.foodtracker.model.ingredient.Location;

import org.junit.Test;

import java.util.Map;


public class LocationUnitTest {
    private Location getMockLocation(){
        Location mockLocation = new Location();
        return mockLocation;
    }
    private Location getMockLocation(String name){
        Location mockLocation = new Location(name);
        return mockLocation;
    }

    @Test
    public void testCategoryConstructors(){
        Location mockLocation1 = getMockLocation();
        Location mockLocation2 = getMockLocation("Pantry");
        assertTrue(mockLocation1.getName() == "");
        assertTrue(mockLocation2.getName() == "Pantry");
    }

    @Test
    public void testGetData(){
        Location mockLocation = getMockLocation("Pantry");
        Map<String,Object> data = mockLocation.getData();
        assertTrue(data.get("name") == "Pantry");
    }

    @Test
    public void testSetAndGetKey(){
        Location mockLocation = getMockLocation();
        mockLocation.setKey("123");
        assertTrue(mockLocation.getKey() == "123");
    }

    @Test
    public void testSetAndGetName(){
        Location mockLocation = getMockLocation();
        mockLocation.setName("Pantry");
        assertTrue(mockLocation.getName() == "Pantry");
    }

    @Test
    public void testHasNonDefaultKey(){
        Location mockLocation = getMockLocation();
        assertTrue(mockLocation.hasNonDefaultKey());
    }

    @Test
    public void testGetCollectionName(){
        Location mockLocation = getMockLocation();
        assertTrue(mockLocation.getCollectionName() == "Ingredients-Location");
    }
}
