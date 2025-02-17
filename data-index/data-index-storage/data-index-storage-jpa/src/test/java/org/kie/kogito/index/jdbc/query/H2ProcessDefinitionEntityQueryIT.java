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
package org.kie.kogito.index.jdbc.query;

import org.kie.kogito.index.jdbc.H2QuarkusTestProfile;
import org.kie.kogito.index.jdbc.JPAProcessDefinitionEntityStorage;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.model.ProcessDefinitionKey;
import org.kie.kogito.index.test.query.AbstractProcessDefinitionQueryIT;
import org.kie.kogito.persistence.api.Storage;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

import jakarta.inject.Inject;

@QuarkusTest
@TestTransaction
@QuarkusTestResource(value = H2DatabaseTestResource.class, restrictToAnnotatedClass = true)
@TestProfile(H2QuarkusTestProfile.class)
class H2ProcessDefinitionEntityQueryIT extends AbstractProcessDefinitionQueryIT {

    @Inject
    JPAProcessDefinitionEntityStorage storage;

    @Override
    public Storage<ProcessDefinitionKey, ProcessDefinition> getStorage() {
        return storage;
    }
}
