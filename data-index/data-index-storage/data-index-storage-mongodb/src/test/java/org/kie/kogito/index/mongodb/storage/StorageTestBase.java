package org.kie.kogito.index.mongodb.storage;

import org.kie.kogito.persistence.api.Storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTestBase<K, V> {

    void testStorage(Storage<K, V> storage, K key, V value1, V value2) {
        assertNull(storage.get(key));
        assertFalse(storage.containsKey(key));

        storage.put(key, value1);
        assertEquals(value1, storage.get(key));
        assertTrue(storage.containsKey(key));

        storage.put(key, value2);
        assertEquals(value2, storage.get(key));
        assertTrue(storage.containsKey(key));

        storage.remove(key);
        assertNull(storage.get(key));
        assertFalse(storage.containsKey(key));
    }
}
