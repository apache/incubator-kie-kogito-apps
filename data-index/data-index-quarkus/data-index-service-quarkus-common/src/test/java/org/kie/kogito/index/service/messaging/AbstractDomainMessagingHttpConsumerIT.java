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
package org.kie.kogito.index.service.messaging;

import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceDataEvent;
import org.kie.kogito.index.model.ProcessInstanceState;

import io.smallrye.reactive.messaging.memory.InMemoryConnector;

import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_PROCESSINSTANCES_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_USERTASKINSTANCES_EVENTS;
import static org.kie.kogito.index.test.TestUtils.getProcessCloudEvent;
import static org.kie.kogito.index.test.TestUtils.getUserTaskCloudEvent;

public abstract class AbstractDomainMessagingHttpConsumerIT extends AbstractDomainMessagingConsumerIT {

    @Inject
    @Any
    public InMemoryConnector connector;

    protected void sendUserTaskInstanceEvent() throws Exception {
        UserTaskInstanceDataEvent<?> event = getUserTaskCloudEvent("45fae435-b098-4f27-97cf-a0c107072e8b", "travels",
                "2308e23d-9998-47e9-a772-a078cf5b891b", null, null, "Completed");
        connector.source(KOGITO_USERTASKINSTANCES_EVENTS).send(event);
    }

    protected void sendProcessInstanceEvent() throws Exception {
        ProcessInstanceDataEvent<?> event = getProcessCloudEvent("travels", "2308e23d-9998-47e9-a772-a078cf5b891b",
                ProcessInstanceState.ACTIVE, null,
                null, null, "currentUser");
        connector.source(KOGITO_PROCESSINSTANCES_EVENTS).send(event);
    }

    protected abstract String getTestProtobufFileContent() throws Exception;
}
