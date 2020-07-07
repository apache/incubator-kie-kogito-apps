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

import javax.inject.Inject;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.mock.MockIndexCreateOrUpdateEventListener;
import org.kie.kogito.index.mongodb.model.DomainEntityMapper;
import org.kie.kogito.index.mongodb.model.JobEntityMapper;
import org.kie.kogito.index.mongodb.model.ProcessIdEntity;
import org.kie.kogito.index.mongodb.model.ProcessIdEntityMapper;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntityMapper;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntityMapper;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.schema.ProcessDescriptor;
import org.kie.kogito.persistence.mongodb.MongoServerTestResource;
import org.kie.kogito.persistence.mongodb.index.ProcessIndexEvent;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.index.Constants.JOBS_STORAGE;
import static org.kie.kogito.index.Constants.PROCESS_ID_MODEL_STORAGE;
import static org.kie.kogito.index.Constants.PROCESS_INSTANCES_STORAGE;
import static org.kie.kogito.index.Constants.USER_TASK_INSTANCES_STORAGE;
import static org.kie.kogito.persistence.mongodb.storage.StorageUtils.getCollection;

@QuarkusTest
@QuarkusTestResource(MongoServerTestResource.class)
class MongoModelServiceImplIT {

    @Inject
    MockIndexCreateOrUpdateEventListener mockIndexCreateOrUpdateEventListener;

    @Inject
    MongoModelServiceImpl mongoModelServiceImpl;

    @AfterEach
    public void tearDown() {
        mockIndexCreateOrUpdateEventListener.reset();
    }

    @Test
    void onProcessIndexEvent() {
        String processId = "testProcess";
        String processType = "testProcessType";

        ProcessDescriptor processDescriptor = new ProcessDescriptor(processId, processType);
        ProcessIndexEvent processIndexEvent = new ProcessIndexEvent(processDescriptor);

        mongoModelServiceImpl.onProcessIndexEvent(processIndexEvent);

        Storage<String, String> processIdStorage = new MongoStorage<>(getCollection(PROCESS_ID_MODEL_STORAGE, ProcessIdEntity.class), String.class.getName(), new ProcessIdEntityMapper());
        assertTrue(processIdStorage.containsKey(processId));
        assertEquals(processType, processIdStorage.get(processId));

        mockIndexCreateOrUpdateEventListener.assertFire("testProcess_domain", processType);
    }

    @Test
    void testInit() {
        mongoModelServiceImpl.init();

        mockIndexCreateOrUpdateEventListener.assertFire(PROCESS_INSTANCES_STORAGE, ProcessInstance.class.getName());
        mockIndexCreateOrUpdateEventListener.assertFire(USER_TASK_INSTANCES_STORAGE, UserTaskInstance.class.getName());
        mockIndexCreateOrUpdateEventListener.assertFire(JOBS_STORAGE, Job.class.getName());
    }

    @Test
    void testGetEntityMapper() {
        assertTrue(mongoModelServiceImpl.getEntityMapper(JOBS_STORAGE) instanceof JobEntityMapper);
        assertTrue(mongoModelServiceImpl.getEntityMapper(PROCESS_INSTANCES_STORAGE) instanceof ProcessInstanceEntityMapper);
        assertTrue(mongoModelServiceImpl.getEntityMapper(USER_TASK_INSTANCES_STORAGE) instanceof UserTaskInstanceEntityMapper);
        assertTrue(mongoModelServiceImpl.getEntityMapper(PROCESS_ID_MODEL_STORAGE) instanceof ProcessIdEntityMapper);
        assertTrue(mongoModelServiceImpl.getEntityMapper("test_domain") instanceof DomainEntityMapper);
    }
}