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
package org.kie.kogito.index.service.json;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.kie.kogito.event.usertask.UserTaskInstanceStateDataEvent;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.index.storage.Constants.KOGITO_DOMAIN_ATTRIBUTE;
import static org.kie.kogito.index.storage.Constants.USER_TASK_INSTANCES_DOMAIN_ATTRIBUTE;
import static org.kie.kogito.index.test.TestUtils.getUserTaskCloudEvent;

public class UserTaskInstanceMetaMapperTest {

    @Test
    public void testUserTaskInstanceMapper() {
        String taskId = UUID.randomUUID().toString();
        String processId = "travels";
        String rootProcessId = "root_travels";
        String processInstanceId = UUID.randomUUID().toString();
        String rootProcessInstanceId = UUID.randomUUID().toString();
        String utPrefix = KOGITO_DOMAIN_ATTRIBUTE + "." + USER_TASK_INSTANCES_DOMAIN_ATTRIBUTE;
        UserTaskInstanceStateDataEvent event = getUserTaskCloudEvent(taskId, processId, processInstanceId, rootProcessInstanceId, rootProcessId, "InProgress");
        ObjectNode json = new UserTaskInstanceMetaMapper().apply(event);
        assertThat(json).isNotNull();
        assertThatJson(json.toString()).and(
                a -> a.node("id").isEqualTo(rootProcessInstanceId),
                a -> a.node("processId").isEqualTo(rootProcessId),
                a -> a.node(KOGITO_DOMAIN_ATTRIBUTE).isNotNull(),
                a -> a.node(KOGITO_DOMAIN_ATTRIBUTE + ".lastUpdate").isEqualTo(event.getTime().toInstant().toEpochMilli()),
                a -> a.node(utPrefix).isArray().hasSize(1),
                a -> a.node(utPrefix + "[0].id").isEqualTo(taskId),
                a -> a.node(utPrefix + "[0].processInstanceId").isEqualTo(processInstanceId),
                a -> a.node(utPrefix + "[0].state").isEqualTo(event.getData().getState()),
                a -> a.node(utPrefix + "[0].description").isEqualTo(event.getData().getUserTaskDescription()),
                a -> a.node(utPrefix + "[0].name").isEqualTo(event.getData().getUserTaskName()),
                a -> a.node(utPrefix + "[0].priority").isEqualTo(event.getData().getUserTaskPriority()),
                a -> a.node(utPrefix + "[0].actualOwner").isEqualTo(event.getData().getActualOwner()),
                a -> a.node(utPrefix + "[0].started").isEqualTo(event.getData().getEventDate().toInstant().toEpochMilli()),
                a -> a.node(utPrefix + "[0].completed").isAbsent(),
                a -> a.node(utPrefix + "[0].lastUpdate").isEqualTo(event.getTime().toInstant().toEpochMilli()));
    }
}
