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

import java.util.Collection;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.kie.kogito.event.DataEvent;
import org.kie.kogito.event.process.ProcessDefinitionDataEvent;
import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceDataEvent;
import org.kie.kogito.index.event.KogitoJobCloudEvent;
import org.kie.kogito.index.service.IndexingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.arc.properties.IfBuildProperty;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Blocking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.*;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_JOBS_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_PROCESSINSTANCES_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_PROCESS_DEFINITIONS_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_USERTASKINSTANCES_EVENTS;

@ApplicationScoped
@IfBuildProperty(name = "kogito.data-index.blocking", stringValue = "true")
public class BlockingMessagingEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockingMessagingEventConsumer.class);

    @Inject
    Event<DataEvent<?>> eventPublisher;

    @Inject
    IndexingService indexingService;

    @Incoming(KOGITO_PROCESSINSTANCES_EVENTS)
    @Blocking
    @Transactional
    public Uni<Void> onProcessInstanceEvent(Object input) {
        if (input instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<ProcessInstanceDataEvent<?>> events = (Collection<ProcessInstanceDataEvent<?>>) input;
            LOGGER.debug("Process instance consumer received grouped ProcessInstanceDataEvents: \n{}", events);
            for (ProcessInstanceDataEvent<?> event : events) {
                handleProcessInstanceEvent(event);
            }
        } else if (input instanceof ProcessInstanceDataEvent) {
            ProcessInstanceDataEvent<?> event = (ProcessInstanceDataEvent<?>) input;
            LOGGER.debug("Process instance consumer received ProcessInstanceDataEvent: \n{}", event);
            handleProcessInstanceEvent(event);
        } else {
            LOGGER.error("Unknown event type received: {}", input.getClass());
        }
        return Uni.createFrom().voidItem();
    }

    @Incoming(KOGITO_USERTASKINSTANCES_EVENTS)
    @Blocking
    @Transactional
    public Uni<Void> onUserTaskInstanceEvent(Object input) {
        if (input instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<UserTaskInstanceDataEvent<?>> events = (Collection<UserTaskInstanceDataEvent<?>>) input;
            LOGGER.debug("UserTask instance consumer received grouped UserTaskInstanceDataEvent: \n{}", events);
            for (UserTaskInstanceDataEvent<?> event : events) {
                handleUserTaskInstanceEvent(event);
            }
        } else if (input instanceof UserTaskInstanceDataEvent) {
            UserTaskInstanceDataEvent<?> event = (UserTaskInstanceDataEvent<?>) input;
            LOGGER.debug("Process instance consumer received UserTaskInstanceDataEvent: \n{}", event);
            handleUserTaskInstanceEvent(event);
        } else {
            LOGGER.error("Unknown event type received: {}", input.getClass());
        }
        return Uni.createFrom().voidItem();
    }

    @Incoming(KOGITO_JOBS_EVENTS)
    @Blocking
    @Transactional
    public Uni<Void> onJobEvent(KogitoJobCloudEvent event) {
        LOGGER.debug("Job received KogitoJobCloudEvent \n{}", event);
        return Uni.createFrom().item(event)
                .onItem().invoke(e -> indexingService.indexJob(e.getData()))
                .onFailure().invoke(t -> LOGGER.error("Error processing job KogitoJobCloudEvent: {}", t.getMessage(), t))
                .onItem().ignore().andContinueWithNull();
    }

    @Incoming(KOGITO_PROCESS_DEFINITIONS_EVENTS)
    @Blocking
    @Transactional
    public Uni<Void> onProcessDefinitionDataEvent(Object input) {
        if (input instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<ProcessDefinitionDataEvent> events = (Collection<ProcessDefinitionDataEvent>) input;
            LOGGER.debug("Process definition instance consumer received grouped ProcessDefinitionDataEvent: \n{}", events);
            for (ProcessDefinitionDataEvent event : events) {
                handleProcessDefinitionEvent(event);
            }
        } else if (input instanceof ProcessDefinitionDataEvent) {
            ProcessDefinitionDataEvent event = (ProcessDefinitionDataEvent) input;
            LOGGER.debug("Process definition consumer received ProcessDefinitionDataEvent: \n{}", event);
            handleProcessDefinitionEvent(event);
        } else {
            LOGGER.error("Unknown event type received: {}", input.getClass());
        }
        return Uni.createFrom().voidItem();
    }

    private void handleProcessInstanceEvent(ProcessInstanceDataEvent<?> event) {
        try {
            indexingService.indexProcessInstanceEvent(event);
            eventPublisher.fire(event);
        } catch (Exception ex) {
            LOGGER.error("Error processing process instance event: {}", event, ex);
        }
    }

    private void handleUserTaskInstanceEvent(UserTaskInstanceDataEvent<?> event) {
        try {
            indexingService.indexUserTaskInstanceEvent(event);
            eventPublisher.fire(event);
        } catch (Exception ex) {
            LOGGER.error("Error processing userTask instance event: {}", event, ex);
        }
    }

    private void handleProcessDefinitionEvent(ProcessDefinitionDataEvent event) {
        try {
            indexingService.indexProcessDefinition(event);
            eventPublisher.fire(event);
        } catch (Exception ex) {
            LOGGER.error("Error processing process definition event: {}", event, ex);
        }
    }
}
