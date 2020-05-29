/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.index.mongodb.storage;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.schema.ProcessDescriptor;
import org.kie.kogito.persistence.mongodb.MongoDBServerTestResource;
import org.kie.kogito.persistence.mongodb.index.IndexCreateOrUpdateEvent;
import org.kie.kogito.persistence.mongodb.index.ProcessIndexEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.index.Constants.JOBS_STORAGE;
import static org.kie.kogito.index.Constants.PROCESS_ID_MODEL_STORAGE;
import static org.kie.kogito.index.Constants.PROCESS_INSTANCES_STORAGE;
import static org.kie.kogito.index.Constants.USER_TASK_INSTANCES_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBServerTestResource.class)
class MongoDBStorageFactoryImplTest {

    @Inject
    MockIndexCreateOrUpdateEventListener mockIndexCreateOrUpdateEventListener;

    @Inject
    MongoDBStorageFactoryImpl storageFactory;

    @Test
    void testGetStorage() {
        Storage<String, ?> processIdStorage = storageFactory.getStorage(PROCESS_ID_MODEL_STORAGE);
        assertTrue(processIdStorage instanceof ProcessIdStorage);

        Storage<String, ?> processInstanceStorage = storageFactory.getStorage(PROCESS_INSTANCES_STORAGE);
        assertTrue(processInstanceStorage instanceof ProcessInstanceStorage);

        Storage<String, ?> userTaskInstanceStorage = storageFactory.getStorage(USER_TASK_INSTANCES_STORAGE);
        assertTrue(userTaskInstanceStorage instanceof UserTaskInstanceStorage);

        Storage<String, ?> jobStorage = storageFactory.getStorage(JOBS_STORAGE);
        assertTrue(jobStorage instanceof JobStorage);
    }

    @Test
    void testGetOrCreateStorage() {
        String invalidStorageName = "test";
        Storage<String, ?> invalidStorage = storageFactory.getOrCreateStorage(invalidStorageName);
        assertNull(invalidStorage);

        String storageName = "test_domain";
        Storage<String, ?> storage = storageFactory.getOrCreateStorage(storageName);
        assertTrue(storage instanceof DomainStorage);
        assertEquals("test", ((DomainStorage) storage).processId);
    }

    @Test
    void onProcessIndexEvent() {
        String processId = "testProcess";
        String processType = "testProcessType";

        ProcessDescriptor processDescriptor = new ProcessDescriptor(processId, processType);
        ProcessIndexEvent processIndexEvent = new ProcessIndexEvent(processDescriptor);

        mockIndexCreateOrUpdateEventListener.setTriggered(false);
        assertFalse(mockIndexCreateOrUpdateEventListener.isTriggered());
        storageFactory.onProcessIndexEvent(processIndexEvent);

        assertTrue(storageFactory.getStorage(PROCESS_ID_MODEL_STORAGE).containsKey(processId));
        assertEquals(processType, storageFactory.getStorage(PROCESS_ID_MODEL_STORAGE).get(processId));
        assertTrue(mockIndexCreateOrUpdateEventListener.isTriggered());
    }

    void onIndexCreateOrUpdateEvent(@Observes IndexCreateOrUpdateEvent event) {

    }
}