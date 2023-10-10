package org.kie.kogito.index.service;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.process.UserTaskInstanceDataEvent;
import org.kie.kogito.index.event.KogitoJobCloudEvent;

import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector;

import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_JOBS_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_PROCESSINSTANCES_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_USERTASKINSTANCES_EVENTS;

public abstract class AbstractIndexingIT {

    @Inject
    @Any
    public InMemoryConnector connector;

    protected void indexProcessCloudEvent(ProcessInstanceDataEvent event) {
        connector.source(KOGITO_PROCESSINSTANCES_EVENTS).send(event);
    }

    protected void indexUserTaskCloudEvent(UserTaskInstanceDataEvent event) {
        connector.source(KOGITO_USERTASKINSTANCES_EVENTS).send(event);
    }

    protected void indexJobCloudEvent(KogitoJobCloudEvent event) {
        connector.source(KOGITO_JOBS_EVENTS).send(event);
    }
}
