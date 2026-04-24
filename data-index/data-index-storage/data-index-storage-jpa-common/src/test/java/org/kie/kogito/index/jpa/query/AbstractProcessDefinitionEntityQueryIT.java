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
package org.kie.kogito.index.jpa.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.kie.kogito.index.jpa.storage.ProcessDefinitionEntityStorage;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.model.ProcessDefinitionKey;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.index.test.query.AbstractProcessDefinitionQueryIT;
import org.kie.kogito.persistence.api.Storage;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.index.json.JsonUtils.jsonFilter;
import static org.kie.kogito.index.test.QueryTestUtils.assertNoKey;
import static org.kie.kogito.index.test.QueryTestUtils.assertWithKey;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.contains;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.equalTo;

public abstract class AbstractProcessDefinitionEntityQueryIT extends AbstractProcessDefinitionQueryIT {

    ProcessDefinitionEntityStorage storage;

    public AbstractProcessDefinitionEntityQueryIT(ProcessDefinitionEntityStorage storage) {
        this.storage = storage;
    }

    @Override
    public Storage<ProcessDefinitionKey, ProcessDefinition> getStorage() {
        return storage;
    }

    @Test
    void testCount() {
        ProcessDefinition pdv1 = TestUtils.createProcessDefinition("items", "1.0", Set.of("admin", "kogito"));
        storage.put(new ProcessDefinitionKey(pdv1.getId(), pdv1.getVersion()), pdv1);
        assertThat(storage.query().count()).isNotZero();
        assertThat(storage.query().filter(List.of(equalTo("version", "60.0"))).count()).isZero();
    }

    @Test
    void testMetadata() {
        final String processId = "persons";
        final String version = "1.0";
        ProcessDefinitionKey key = new ProcessDefinitionKey(processId, version);
        ProcessDefinition definitionEvent = TestUtils.createProcessDefinition(processId, version, Set.of());
        definitionEvent.setMetadata(Map.of("name", "Javierito", "hobbies", List.of("community", "first")));
        Storage<ProcessDefinitionKey, ProcessDefinition> storage = getStorage();
        storage.put(key, definitionEvent);
        queryAndAssert(assertWithKey(), storage, singletonList(jsonFilter(equalTo("metadata.name", "Javierito"))), null, null, null, key);
        queryAndAssert(assertNoKey(), storage, singletonList(jsonFilter(equalTo("metadata.name", "Fulanito"))), null, null, null, key);
        queryAndAssert(assertWithKey(), storage, singletonList(jsonFilter(contains("metadata.hobbies", "community"))), null, null, null, key);
        queryAndAssert(assertNoKey(), storage, singletonList(jsonFilter(contains("metadata.hobbies", "commercial"))), null, null, null, key);
    }

}
