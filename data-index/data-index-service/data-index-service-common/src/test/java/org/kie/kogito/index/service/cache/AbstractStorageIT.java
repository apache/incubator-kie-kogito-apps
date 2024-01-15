/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.index.service.cache;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.ProcessInstanceState;
import org.kie.kogito.index.storage.DataIndexStorageService;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;

import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.index.storage.Constants.PROCESS_INSTANCES_STORAGE;
import static org.kie.kogito.index.test.TestUtils.getProcessInstance;

public abstract class AbstractStorageIT {

    @Inject
    public DataIndexStorageService cacheService;

    @Inject
    public StorageService storageService;

    @Test
    void testObjectCreatedListener() throws Exception {
        String processId = "travels";
        String processInstanceId = UUID.randomUUID().toString();

        CompletableFuture<ProcessInstance> cf = new CompletableFuture<>();
        Storage<String, ProcessInstance> cache = storageService.getCache(PROCESS_INSTANCES_STORAGE, ProcessInstance.class);
        cache.objectCreatedListener().subscribe().with(pi -> cf.complete(pi));
        cache.put(processInstanceId, getProcessInstance(processId, processInstanceId, ProcessInstanceState.ACTIVE.ordinal(), null, null));

        ProcessInstance pi = cf.get(1, TimeUnit.MINUTES);
        assertThat(pi).hasFieldOrPropertyWithValue("id", processInstanceId).hasFieldOrPropertyWithValue("processId", processId);
    }

    @Test
    void testObjectUpdatedListener() throws Exception {
        String processId = "travels";
        String processInstanceId = UUID.randomUUID().toString();

        CompletableFuture<ProcessInstance> cf = new CompletableFuture<>();
        Storage<String, ProcessInstance> cache = storageService.getCache(PROCESS_INSTANCES_STORAGE, ProcessInstance.class);
        cache.objectUpdatedListener().subscribe().with(pi -> cf.complete(pi));
        cache.put(processInstanceId, getProcessInstance(processId, processInstanceId, ProcessInstanceState.ACTIVE.ordinal(), null, null));
        cache.put(processInstanceId, getProcessInstance(processId, processInstanceId, ProcessInstanceState.COMPLETED.ordinal(), null, null));

        ProcessInstance pi = cf.get(1, TimeUnit.MINUTES);
        assertThat(pi).hasFieldOrPropertyWithValue("id", processInstanceId).hasFieldOrPropertyWithValue("state", ProcessInstanceState.COMPLETED.ordinal());
    }

    @Test
    void testObjectRemovedListener() throws Exception {
        String processId = "travels";
        String processInstanceId = UUID.randomUUID().toString();

        CompletableFuture<String> cf = new CompletableFuture<>();
        Storage<String, ProcessInstance> cache = storageService.getCache(PROCESS_INSTANCES_STORAGE, ProcessInstance.class);
        cache.objectRemovedListener().subscribe().with(id -> cf.complete(id));
        cache.put(processInstanceId, getProcessInstance(processId, processInstanceId, ProcessInstanceState.ACTIVE.ordinal(), null, null));
        cache.remove(processInstanceId);

        String id = cf.get(1, TimeUnit.MINUTES);
        assertThat(id).isEqualTo(processInstanceId);
    }

    @AfterEach
    void tearDown() {
        cacheService.getProcessDefinitionStorage().clear();
        cacheService.getProcessInstanceStorage().clear();
    }
}
