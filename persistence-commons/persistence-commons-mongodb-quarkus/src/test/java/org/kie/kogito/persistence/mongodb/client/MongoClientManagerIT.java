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
package org.kie.kogito.persistence.mongodb.client;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import com.mongodb.client.MongoCollection;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class MongoClientManagerIT {

    @Inject
    MongoClientManager mongoClientManager;

    @Test
    void testGetCollection() {
        MongoCollection<Document> mongoCollection = mongoClientManager.getCollection("test");
        assertEquals("test", mongoCollection.getNamespace().getCollectionName());
    }

    @Test
    void testGetCollection_withDocumentClass() {
        MongoCollection<TestClass> mongoCollection = mongoClientManager.getCollection("test", TestClass.class);
        assertEquals("test", mongoCollection.getNamespace().getCollectionName());
        assertTrue(mongoCollection.getDocumentClass().isAssignableFrom(TestClass.class));
    }

    private static class TestClass {

    }
}
