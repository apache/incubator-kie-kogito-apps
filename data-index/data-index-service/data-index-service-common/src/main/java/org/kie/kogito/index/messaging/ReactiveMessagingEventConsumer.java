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
package org.kie.kogito.index.messaging;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.kie.kogito.index.event.KogitoCloudEvent;
import org.kie.kogito.index.event.KogitoJobCloudEvent;
import org.kie.kogito.index.event.KogitoProcessCloudEvent;
import org.kie.kogito.index.event.KogitoUserTaskCloudEvent;
import org.kie.kogito.index.service.IndexingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.arc.properties.UnlessBuildProperty;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
@UnlessBuildProperty(name = "kogito.data-index.blocking", stringValue = "true", enableIfMissing = true)
public class ReactiveMessagingEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveMessagingEventConsumer.class);

    public static final String KOGITO_PROCESSINSTANCES_EVENTS = "kogito-processinstances-events";
    public static final String KOGITO_USERTASKINSTANCES_EVENTS = "kogito-usertaskinstances-events";
    public static final String KOGITO_JOBS_EVENTS = "kogito-jobs-events";

    @Inject
    IndexingService indexingService;

    @Inject
    Event<KogitoCloudEvent> eventPublisher;

    @Incoming(KOGITO_PROCESSINSTANCES_EVENTS)
    public Uni<Void> onProcessInstanceEvent(KogitoProcessCloudEvent event) {
        LOGGER.debug("Process instance consumer received KogitoCloudEvent: \n{}", event);
        return Uni.createFrom().item(event)
                .invoke(e -> indexingService.indexProcessInstance(e.getData()))
                .invoke(e -> eventPublisher.fire(e))
                .onFailure()
                .invoke(t -> LOGGER.error("Error processing process instance KogitoCloudEvent: {}", t.getMessage(), t))
                .onItem().ignore().andContinueWithNull();
    }

    @Incoming(KOGITO_USERTASKINSTANCES_EVENTS)
    public Uni<Void> onUserTaskInstanceEvent(KogitoUserTaskCloudEvent event) {
        LOGGER.debug("Task instance received KogitoUserTaskCloudEvent \n{}", event);
        return Uni.createFrom().item(event)
                .invoke(e -> indexingService.indexUserTaskInstance(e.getData()))
                .invoke(e -> eventPublisher.fire(e))
                .onFailure()
                .invoke(t -> LOGGER.error("Error processing task instance KogitoUserTaskCloudEvent: {}", t.getMessage(), t))
                .onItem().ignore().andContinueWithNull();
    }

    @Incoming(KOGITO_JOBS_EVENTS)
    public Uni<Void> onJobEvent(KogitoJobCloudEvent event) {
        LOGGER.debug("Job received KogitoJobCloudEvent \n{}", event);
        return Uni.createFrom().item(event)
                .onItem().invoke(e -> indexingService.indexJob(e.getData()))
                .onFailure().invoke(t -> LOGGER.error("Error processing job KogitoJobCloudEvent: {}", t.getMessage(), t))
                .onItem().ignore().andContinueWithNull();
    }

}
