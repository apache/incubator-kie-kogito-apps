package org.kie.kogito.trusty.storage.postgresql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.postgresql.PostgresStorage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BaseTransactionalStorageTest {

    private static final String KEY = "KEY";
    private static final String VALUE = "VALUE";

    private static class StringStorage extends BaseTransactionalStorage<String> {

        public StringStorage(final PostgresStorage<String> delegate) {
            super(delegate);
        }
    }

    private static PostgresStorage<String> delegate;

    private static BaseTransactionalStorage<String> storage;

    @BeforeAll
    @SuppressWarnings("unchecked")
    public static void setup() {
        delegate = mock(PostgresStorage.class);
        storage = new StringStorage(delegate);
    }

    @Test
    public void testObjectCreatedListener() {
        storage.objectCreatedListener();
        verify(delegate).objectCreatedListener();
    }

    @Test
    public void testObjectUpdatedListener() {
        storage.objectUpdatedListener();
        verify(delegate).objectUpdatedListener();
    }

    @Test
    public void testObjectRemovedListener() {
        storage.objectRemovedListener();
        verify(delegate).objectRemovedListener();
    }

    @Test
    public void testQuery() {
        storage.query();
        verify(delegate).query();
    }

    @Test
    public void testGet() {
        storage.get(KEY);
        verify(delegate).get(KEY);
    }

    @Test
    public void testPut() {
        storage.put(KEY, VALUE);
        verify(delegate).put(KEY, VALUE);
    }

    @Test
    public void testRemove() {
        storage.remove(KEY);
        verify(delegate).remove(KEY);
    }

    @Test
    public void testContainsKey() {
        storage.containsKey(KEY);
        verify(delegate).containsKey(KEY);
    }

    @Test
    public void testEntries() {
        storage.entries();
        verify(delegate).entries();
    }

    @Test
    public void testClear() {
        storage.clear();
        verify(delegate).clear();
    }

    @Test
    public void testGetRootType() {
        storage.getRootType();
        verify(delegate).getRootType();
    }
}
