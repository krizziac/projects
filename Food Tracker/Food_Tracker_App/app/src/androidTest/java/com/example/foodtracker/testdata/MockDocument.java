package com.example.foodtracker.testdata;

import com.example.foodtracker.model.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Mock document class used for collection testing
 */
public class MockDocument extends Document {

    public static final String FIREBASE_INTEGRATION_TEST = "FIREBASE_INTEGRATION_TEST";
    private int field1 = new Random().nextInt();
    private boolean field2 = new Random().nextBoolean();

    /**
     * Each {@link MockDocument} will be generated to have its own mock data to post to Firestore
     * through the {@link Document#getData()} method.
     */
    public MockDocument() {
    }

    @Override
    public String getCollectionName() {
        return FIREBASE_INTEGRATION_TEST;
    }

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> mockData = new HashMap<>();
        mockData.put(FieldNames.FIELD_1, field1);
        mockData.put(FieldNames.FIELD_2, field2);
        return mockData;
    }

    public boolean isField2() {
        return field2;
    }

    /**
     * Used by Firestore to map a document to a {@link MockDocument}
     */
    public void setField2(boolean field2) {
        this.field2 = field2;
    }

    public int getField1() {
        return field1;
    }

    /**
     * Used by Firestore to map a document to a {@link MockDocument}
     */
    public void setField1(int field1) {
        this.field1 = field1;
    }

    public static class FieldNames {
        public static String FIELD_1 = "field1";
        public static String FIELD_2 = "field2";
    }
}
