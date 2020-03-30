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

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.cache.Cache;
import org.kie.kogito.index.cache.CacheService;
import org.kie.kogito.index.mongodb.MongoDBServerTestResource;
import org.kie.kogito.index.mongodb.TestUtils;

@QuarkusTest
@QuarkusTestResource(MongoDBServerTestResource.class)
public class DomainCacheTest {

    @Inject
    CacheService cacheService;

    Cache<String, ObjectNode> cache;

    @BeforeEach
    void setUp() {
        this.cache = cacheService.getDomainModelCache("travels");
    }

    @AfterEach
    void tearDown() {
        cache.clear();
        Assert.assertTrue(cache.isEmpty());
    }

    @Test
    void testCache() {
        String processInstanceId = UUID.randomUUID().toString();
        ObjectNode node1 = TestUtils.createDomainData("John", "Doe");
        ObjectNode node2 = TestUtils.createDomainData("Jane", "Toe");
        CacheTestBase.testCache(cache, processInstanceId, node1, node2);
    }
}
