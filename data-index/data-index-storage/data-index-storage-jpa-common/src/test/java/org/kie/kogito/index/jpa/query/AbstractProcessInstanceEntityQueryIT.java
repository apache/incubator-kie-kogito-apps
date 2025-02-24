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
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.kie.kogito.event.process.ProcessInstanceVariableDataEvent;
import org.kie.kogito.index.jpa.storage.ProcessInstanceEntityStorage;
import org.kie.kogito.index.storage.ProcessInstanceStorage;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.index.test.query.AbstractProcessInstanceQueryIT;

import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractProcessInstanceEntityQueryIT extends AbstractProcessInstanceQueryIT {

    @Inject
    ProcessInstanceEntityStorage storage;

    @Override
    public ProcessInstanceEntityStorage getStorage() {
        return storage;
    }

    @Test
    void testCount() {
        String processId = "persons";
        String processInstanceId = UUID.randomUUID().toString();
        ProcessInstanceStorage storage = getStorage();
        ProcessInstanceVariableDataEvent variableEvent = TestUtils.createProcessInstanceVariableEvent(processInstanceId, processId, "John", 28, false,
                List.of("Super", "Astonishing", "TheRealThing"));
        storage.indexVariable(variableEvent);
        assertThat(storage.query().count()).isOne();
    }
}
