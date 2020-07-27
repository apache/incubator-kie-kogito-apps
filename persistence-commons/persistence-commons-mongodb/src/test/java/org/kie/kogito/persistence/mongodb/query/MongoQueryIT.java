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

package org.kie.kogito.persistence.mongodb.query;

import java.util.List;

import javax.inject.Inject;

import com.mongodb.client.MongoCollection;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.query.QueryFilterFactory;
import org.kie.kogito.persistence.mongodb.MongoServerTestResource;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.mock.MockMongoEntityMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.orderBy;
import static org.kie.kogito.persistence.api.query.SortDirection.ASC;
import static org.kie.kogito.persistence.mongodb.mock.MockMongoEntityMapper.TEST_ATTRIBUTE;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MONGO_ID;

@QuarkusTest
@QuarkusTestResource(MongoServerTestResource.class)
class MongoQueryIT {

    @Inject
    MongoClientManager mongoClientManager;

    MongoQuery<String, Document> mongoQuery;

    MongoCollection<Document> collection;

    @BeforeEach
    void setup() {
        collection = mongoClientManager.getCollection("test", Document.class);
        mongoQuery = new MongoQuery<>(collection, new MockMongoEntityMapper());
    }

    @AfterEach
    void tearDown() {
        collection.drop();
    }

    @Test
    void testExecute() {
        collection.insertOne(new Document(MONGO_ID, "1").append(TEST_ATTRIBUTE, "2"));
        collection.insertOne(new Document(MONGO_ID, "2").append(TEST_ATTRIBUTE, "5"));
        collection.insertOne(new Document(MONGO_ID, "3").append(TEST_ATTRIBUTE, "7"));
        collection.insertOne(new Document(MONGO_ID, "4").append(TEST_ATTRIBUTE, "10"));
        collection.insertOne(new Document(MONGO_ID, "5").append(TEST_ATTRIBUTE, "11"));

        mongoQuery.limit(1);
        mongoQuery.offset(1);
        mongoQuery.sort(List.of(orderBy(TEST_ATTRIBUTE, ASC)));
        mongoQuery.filter(List.of(QueryFilterFactory.in(TEST_ATTRIBUTE, List.of("2", "5", "7"))));

        List<String> results = mongoQuery.execute();
        assertEquals(1, results.size());
        assertEquals("5", results.get(0));
    }
}