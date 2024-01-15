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
package org.kie.kogito.index.mongodb.query;

import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntity;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntityMapper;
import org.kie.kogito.index.storage.ModelUserTaskInstanceStorage;
import org.kie.kogito.index.storage.UserTaskInstanceStorage;
import org.kie.kogito.index.test.query.AbstractUserTaskInstanceQueryIT;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import jakarta.inject.Inject;

import static org.kie.kogito.index.storage.Constants.USER_TASK_INSTANCES_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class UserTaskInstanceQueryIT extends AbstractUserTaskInstanceQueryIT {

    @Inject
    MongoClientManager mongoClientManager;

    UserTaskInstanceStorage storage;

    @BeforeEach
    void setUp() {
        this.storage = new ModelUserTaskInstanceStorage(new MongoStorage<>(mongoClientManager.getCollection(USER_TASK_INSTANCES_STORAGE, UserTaskInstanceEntity.class),
                UserTaskInstance.class.getName(), new UserTaskInstanceEntityMapper()));
    }

    @Override
    public UserTaskInstanceStorage getStorage() {
        return storage;
    }

}
