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

package org.kie.kogito.index.mongodb.cache;

import java.util.UUID;

import javax.inject.Inject;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.cache.Cache;
import org.kie.kogito.index.cache.CacheService;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.MongoDBServerTestResource;
import org.kie.kogito.index.mongodb.TestUtils;

@QuarkusTest
@QuarkusTestResource(MongoDBServerTestResource.class)
public class UserTaskInstanceCacheTest {

    @Inject
    CacheService cacheService;

    Cache<String, UserTaskInstance> cache;

    @BeforeEach
    void setUp() {
        this.cache = cacheService.getUserTaskInstancesCache();
    }

    @AfterEach
    void tearDown() {
        cache.clear();
        Assert.assertTrue(cache.isEmpty());
    }

    @Test
    void testCache() {
        String taskId = UUID.randomUUID().toString();
        String processInstanceId = UUID.randomUUID().toString();
        UserTaskInstance userTaskInstance1 = TestUtils.createUserTaskInstance(taskId, processInstanceId, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(), RandomStringUtils.randomAlphabetic(10), "InProgress");
        UserTaskInstance userTaskInstance2 = TestUtils.createUserTaskInstance(taskId, processInstanceId, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(), RandomStringUtils.randomAlphabetic(10), "Completed");
        CacheTestBase.testCache(cache, taskId, userTaskInstance1, userTaskInstance2);
    }
}
