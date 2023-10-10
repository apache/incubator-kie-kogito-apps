package org.kie.kogito.index.service.messaging;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.process.UserTaskInstanceDataEvent;
import org.kie.kogito.index.event.KogitoJobCloudEvent;
import org.kie.kogito.index.model.ProcessInstanceState;

import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector;

import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.*;
import static org.kie.kogito.index.test.TestUtils.*;

public abstract class AbstractMessagingHttpConsumerIT extends AbstractMessagingConsumerIT {

    @Inject
    @Any
    public InMemoryConnector connector;

    protected void sendUserTaskInstanceEvent() throws Exception {
        UserTaskInstanceDataEvent event = getUserTaskCloudEvent("45fae435-b098-4f27-97cf-a0c107072e8b", "travels",
                "2308e23d-9998-47e9-a772-a078cf5b891b", null, null, "Completed");
        connector.source(KOGITO_USERTASKINSTANCES_EVENTS).send(event);
    }

    protected void sendProcessInstanceEvent() throws Exception {
        ProcessInstanceDataEvent event = getProcessCloudEvent("travels", "2308e23d-9998-47e9-a772-a078cf5b891b",
                ProcessInstanceState.ACTIVE, null,
                null, null, "currentUser");
        connector.source(KOGITO_PROCESSINSTANCES_EVENTS).send(event);
    }

    protected void sendJobEvent() throws Exception {
        KogitoJobCloudEvent event = getJobCloudEvent("8350b8b6-c5d9-432d-a339-a9fc85f642d4_0", "travels",
                "7c1d9b38-b462-47c5-8bf2-d9154f54957b", null, null, "SCHEDULED");
        connector.source(KOGITO_JOBS_EVENTS).send(event);
    }

}
