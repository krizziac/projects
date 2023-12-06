package com.example.foodtracker.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.foodtracker.model.Document;
import com.example.foodtracker.testdata.MockDocument;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tests the integration of the {@link Collection} class with {@link com.google.firebase.firestore.FirebaseFirestore}
 * generally these tests should be skipped since they are asynchronous and can take a while
 */
@Ignore("Asynchronous tests to test Firestore integration, run one test at a time to test")
public class CollectionIntegrationTest {

    public static Collection<MockDocument> collection;

    /**
     * Calls the Firebase recursive delete function in order to clear the collection
     * Allows the tests to be independent of each other
     */
    @AfterClass
    public static void after() {
        AtomicBoolean done = new AtomicBoolean(false);
        Task<QuerySnapshot> querySnapshotTask = FirebaseFirestore.getInstance().collection(MockDocument.FIREBASE_INTEGRATION_TEST).get();
        querySnapshotTask.addOnSuccessListener(result -> {
            for (int i = 0; i < result.size(); i++) {
                Task<Void> delete = result.getDocuments().get(i).getReference().delete();
                if (i == result.size()) {
                    delete.addOnSuccessListener(deleted -> done.set(true));
                }
            }
        });
        waitForTimeoutOrSuccess(done);
    }

    /**
     * Waits until done evaluates to true, should only be used on tests with timeouts
     */
    private static void waitForTimeoutOrSuccess(AtomicBoolean done) {
        while (!done.get()) {
        }
    }

    @BeforeClass
    public static void setUp() {
        collection = new Collection<>(MockDocument.class, new MockDocument());
    }

    @Test(timeout = 2500)
    public void whenCreateDocuments_thenGetAll_shouldReturnAllCreatedDocumentsWithGeneratedIds() {
        AtomicBoolean done = new AtomicBoolean(false);
        MockDocument mockDocument1 = new MockDocument();
        MockDocument mockDocument2 = new MockDocument();
        List<MockDocument> expectedMockDocuments = new ArrayList<>();
        expectedMockDocuments.add(mockDocument1);
        expectedMockDocuments.add(mockDocument2);
        collection.createDocument(mockDocument1, () -> collection.createDocument(mockDocument2, () -> {
            assertNotNull(mockDocument1.getKey());
            assertNotNull(mockDocument2.getKey());
            collection.getAll(list -> {
                expectedMockDocuments.sort(Comparator.comparing(Document::getKey));
                list.sort(Comparator.comparing(Document::getKey));
                assertEquals(expectedMockDocuments.size(), list.size());
                assertEquals(expectedMockDocuments.get(0).getKey(), list.get(0).getKey());
                assertEquals(expectedMockDocuments.get(1).getKey(), list.get(1).getKey());
                done.set(true);
            });
        }));
        waitForTimeoutOrSuccess(done);
    }

    @Test(timeout = 2500)
    public void whenCreateThenDeleteDocument_whenGetAll_shouldReturnNone() {
        AtomicBoolean done = new AtomicBoolean(false);
        MockDocument mockDocument = new MockDocument();
        collection.createDocument(mockDocument, () -> collection.delete(mockDocument, () -> collection.getAll(list -> {
            assertEquals(0, list.size());
            done.set(true);
        })));
        waitForTimeoutOrSuccess(done);
    }

    @Test(timeout = 2500)
    public void whenCreate_thenCanEditDocument() {
        AtomicBoolean done = new AtomicBoolean(false);
        MockDocument mockDocument = new MockDocument();
        final int initialField1 = mockDocument.getField1();
        final boolean initialField2 = mockDocument.isField2();
        final int editedField1 = -initialField1;
        final boolean editedField2 = !initialField2;
        collection.createDocument(mockDocument, () -> {
            mockDocument.setField1(editedField1);
            mockDocument.setField2(editedField2);
            collection.updateDocument(mockDocument, () -> collection.getAll(list -> {
                assertEquals(1, list.size());
                assertEquals(editedField1, list.get(0).getField1());
                assertEquals(editedField2, list.get(0).isField2());
                done.set(true);
            }));
        });
        waitForTimeoutOrSuccess(done);
    }
}


