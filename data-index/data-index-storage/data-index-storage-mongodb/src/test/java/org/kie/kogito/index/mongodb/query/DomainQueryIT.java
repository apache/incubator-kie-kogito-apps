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

package org.kie.kogito.index.mongodb.query;

import java.util.UUID;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.mongodb.TestUtils;
import org.kie.kogito.index.mongodb.model.DomainEntityMapper;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.SortDirection;
import org.kie.kogito.persistence.mongodb.MongoServerTestResource;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.kie.kogito.index.mongodb.query.QueryTestUtils.assertWithObjectNode;
import static org.kie.kogito.index.mongodb.query.QueryTestUtils.assertWithObjectNodeInOrder;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.and;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.between;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.contains;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.containsAll;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.containsAny;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.equalTo;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.greaterThan;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.greaterThanEqual;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.in;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.isNull;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.lessThan;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.lessThanEqual;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.like;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.notNull;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.or;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.orderBy;

@QuarkusTest
@QuarkusTestResource(MongoServerTestResource.class)
class DomainQueryIT extends QueryTestBase<String, ObjectNode> {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, ObjectNode> storage;

    @BeforeEach
    void setUp() {
        this.storage = new MongoStorage<>(mongoClientManager.getCollection("travels_domain", Document.class),
                                          mongoClientManager.getReactiveCollection("travels_domain", Document.class),
                                          "org.acme.travels.travels.Travels", new DomainEntityMapper());
    }

    @AfterEach
    void tearDown() {
        storage.clear();
    }

    @Test
    void test() {
        String processInstanceId1 = UUID.randomUUID().toString();
        String processInstanceId2 = UUID.randomUUID().toString();

        ObjectNode node1 = TestUtils.createDomainData(processInstanceId1, "John", "Doe");
        ObjectNode node2 = TestUtils.createDomainData(processInstanceId2, "Jane", "Toe");
        storage.put(processInstanceId1, node1);
        storage.put(processInstanceId2, node2);

        queryAndAssert(assertWithObjectNode(), storage, singletonList(in("traveller.firstName", asList("John", "Jane"))), null, null, null, processInstanceId1, processInstanceId2);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(equalTo("traveller.firstName", "John")), null, null, null, processInstanceId1);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(greaterThan("traveller.age", 27)), null, null, null);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(greaterThanEqual("traveller.age", 27)), null, null, null, processInstanceId1, processInstanceId2);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(lessThan("traveller.age", 27)), null, null, null);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(lessThanEqual("traveller.age", 27)), null, null, null, processInstanceId1, processInstanceId2);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(between("traveller.age", 27, 28)), null, null, null, processInstanceId1, processInstanceId2);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(isNull("traveller.age")), null, null, null);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(notNull("traveller.age")), null, null, null, processInstanceId1, processInstanceId2);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(contains("_id", processInstanceId2)), null, null, null, processInstanceId2);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(containsAny("_id", asList(processInstanceId1, processInstanceId2))), null, null, null, processInstanceId1, processInstanceId2);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(containsAll("_id", asList(processInstanceId1, processInstanceId2))), null, null, null);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(like("traveller.firstName", "*hn")), null, null, null, processInstanceId1);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(and(asList(equalTo("traveller.firstName", "John"), equalTo("traveller.lastName", "Doe")))), null, null, null, processInstanceId1);
        queryAndAssert(assertWithObjectNode(), storage, singletonList(or(asList(equalTo("traveller.firstName", "John"), equalTo("traveller.firstName", "Jane")))), null, null, null, processInstanceId1, processInstanceId2);
        queryAndAssert(assertWithObjectNode(), storage, asList(equalTo("traveller.firstName", "John"), equalTo("traveller.lastName", "Toe")), null, null, null);

        queryAndAssert(assertWithObjectNodeInOrder(), storage, asList(in("traveller.firstName", asList("Jane", "John")), in("traveller.lastName", asList("Doe", "Toe"))), singletonList(orderBy("traveller.lastName", SortDirection.ASC)), 1, 1, processInstanceId2);
        queryAndAssert(assertWithObjectNodeInOrder(), storage, null, singletonList(orderBy("traveller.firstName", SortDirection.ASC)), null, null, processInstanceId2, processInstanceId1);
        queryAndAssert(assertWithObjectNodeInOrder(), storage, null, null, 1, 1, processInstanceId2);
        queryAndAssert(assertWithObjectNodeInOrder(), storage, null, asList(orderBy("traveller.firstName", SortDirection.DESC), orderBy("traveller.lastName", SortDirection.ASC)), 1, 1, processInstanceId2);
    }
}
