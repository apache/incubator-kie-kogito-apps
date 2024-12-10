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
package org.kie.kogito.index.postgresql.query;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.kie.kogito.event.process.ProcessInstanceVariableDataEvent;
import org.kie.kogito.index.jpa.query.AbstractProcessInstanceEntityQueryIT;
import org.kie.kogito.index.storage.ProcessInstanceStorage;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.index.json.JsonUtils.jsonFilter;
import static org.kie.kogito.index.test.QueryTestUtils.assertWithId;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.equalTo;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
class ProcessInstanceEntityQueryIT extends AbstractProcessInstanceEntityQueryIT {

    @Test
    void testProcessInstanceVariables() {
        String processId = "travels";
        String processInstanceId = UUID.randomUUID().toString();
        ProcessInstanceStorage storage = getStorage();
        ProcessInstanceVariableDataEvent variableEvent = TestUtils.createProcessInstanceVariableEvent(processInstanceId, processId, "John", "Smith");
        storage.indexVariable(variableEvent);
        queryAndAssert(assertWithId(), storage, singletonList(jsonFilter(equalTo("variables.traveller.firstName", "John"))), null, null, null,
                processInstanceId);
        queryAndAssert((instances, ids) -> assertThat(instances).isEmpty(), storage, singletonList(jsonFilter(equalTo("variables.traveller.firstName", "Smith"))), null, null, null,
                processInstanceId);
    }
}
