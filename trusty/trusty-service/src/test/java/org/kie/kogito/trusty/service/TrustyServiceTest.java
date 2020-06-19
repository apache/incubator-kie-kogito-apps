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

package org.kie.kogito.trusty.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.infinispan.cache.StorageImpl;
import org.kie.kogito.trusty.service.mocks.StorageImplMock;
import org.kie.kogito.trusty.storage.api.TrustyStorageService;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.api.model.Execution;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
public class TrustyServiceTest {

    @Inject
    TrustyService trustyService;

    @InjectMock
    TrustyStorageService storageService;

    @Test
    void GivenADecision_WhenStoreDecisionIsCalled_ThenNoExceptionsAreThrown() {
        Decision decision = new Decision();
        Storage storageMock = mock(Storage.class);
        when(storageMock.put(any(Object.class), any(Object.class))).thenReturn(decision);

        when(storageService.getDecisionsStorage()).thenReturn(storageMock);
        Assertions.assertDoesNotThrow(() -> trustyService.storeDecision("test", decision));
    }

    @Test
    void GivenADecision_WhenADecisionIsStoredAndRetrieved_ThenTheOriginalObjectIsReturned() {
        String executionId = "executionId";
        Decision decision = new Decision();
        decision.setExecutionId(executionId);

        Query queryMock = mock(Query.class);
        when(queryMock.filter(any(List.class))).thenReturn(queryMock);
        when(queryMock.limit(any(Integer.class))).thenReturn(queryMock);
        when(queryMock.offset(any(Integer.class))).thenReturn(queryMock);
        when(queryMock.execute()).thenReturn(List.of(decision));

        Storage storageMock = mock(Storage.class);
        when(storageMock.put(eq(executionId), any(Object.class))).thenReturn(decision);
        when(storageMock.containsKey(eq(executionId))).thenReturn(false);
        when(storageMock.query()).thenReturn(queryMock);

        when(storageService.getDecisionsStorage()).thenReturn(storageMock);

        trustyService.storeDecision("executionId", decision);

        List<Execution> result = trustyService.getExecutionHeaders(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), 100, 0, "");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(decision.getExecutionId(), result.get(0).getExecutionId());
    }

    @Test
    void GivenADecision_WhenADecisionIsStoredAndRetrievedById_ThenTheOriginalObjectIsReturned() {
        String executionId = "executionId";
        Decision decision = new Decision();
        decision.setExecutionId(executionId);

        Storage storageMock = new StorageImplMock(Decision.class);

        when(storageService.getDecisionsStorage()).thenReturn(storageMock);

        trustyService.storeDecision(executionId, decision);

        Decision result = trustyService.getDecisionById(executionId);

        Assertions.assertEquals(executionId, result.getExecutionId());
    }
}
