package org.kie.kogito.persistence.infinispan.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.infinispan.client.hotrod.DefaultTemplate;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.testcontainers.quarkus.InfinispanQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(InfinispanQuarkusTestResource.class)
class InfinispanStorageIT {

    RemoteCache<String, String> cache;

    @Inject
    RemoteCacheManager remoteCacheManager;

    InfinispanStorage<String, String> storage;

    @BeforeEach
    void setup() {
        cache = remoteCacheManager.administration().createCache("test", DefaultTemplate.LOCAL);
        storage = new InfinispanStorage<>(cache, String.class.getName());
    }

    @AfterEach
    void tearDown() {
        cache.clear();
        remoteCacheManager.administration().removeCache("test");
    }

    @Test
    void testContainsKey() {
        String key = "akey";
        String value = "avalue";
        cache.put(key, value);
        assertTrue(storage.containsKey(key));
    }

    @Test
    void testGet() {
        String key = "testGet";
        String value = "testValue";
        cache.put(key, value);
        assertThat(storage.get(key)).isEqualTo(value);
    }

    @Test
    void testPut() {
        String key = "testPut";
        String value = "testValue";
        storage.put(key, value);

        assertThat(cache.get(key)).isEqualTo(value);
    }

    @Test
    void testClear() {
        String key = "testClear";
        String value = "testValue";
        cache.put(key, value);
        storage.clear();
        assertThat(cache.size()).isZero();
    }

    @Test
    void testRemove() {
        String key = "testRemove";
        String value = "testValue";
        cache.put(key, value);
        storage.remove(key);
        assertThat(cache.size()).isZero();
    }

    @Test
    void testObjectCreatedListener() throws Exception {
        CountDownLatch latch = new CountDownLatch(3);
        List<String> values = new ArrayList<>();
        storage.objectCreatedListener().subscribe().with(v -> {
            values.add(v);
            latch.countDown();
        });
        storage.put("testKey_insert_1", "testValue1");
        storage.put("testKey_insert_2", "testValue2");
        storage.put("testKey_insert_3", "testValue3");

        latch.await(1, TimeUnit.MINUTES);
        assertThat(values).hasSize(3).containsExactlyInAnyOrder("testValue3", "testValue2", "testValue1");
    }

    @Test
    void testObjectUpdatedListener() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        List<String> values = new ArrayList<>();
        storage.objectUpdatedListener().subscribe().with(v -> {
            values.add(v);
            latch.countDown();
        });
        storage.put("testKey_update_1", "testValue1");
        storage.put("testKey_update_1", "testValue2");
        storage.put("testKey_update_2", "testValue3");
        storage.put("testKey_update_2", "testValue4");

        latch.await(1, TimeUnit.MINUTES);
        assertThat(values).hasSize(2).containsExactlyInAnyOrder("testValue2", "testValue4");
    }

    @Test
    void testObjectRemovedListener() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        List<String> values = new ArrayList<>();
        storage.objectRemovedListener().subscribe().with(v -> {
            values.add(v);
            latch.countDown();
        });
        storage.put("testKey_remove_1", "testValue1");
        storage.put("testKey_remove_2", "testValue2");
        storage.remove("testKey_remove_1");
        storage.remove("testKey_remove_2");

        latch.await(1, TimeUnit.MINUTES);
        assertThat(values).hasSize(2).containsExactlyInAnyOrder("testKey_remove_1", "testKey_remove_2");
    }
}
