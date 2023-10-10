package org.kie.kogito.index.service.messaging;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.process.UserTaskInstanceDataEvent;
import org.kie.kogito.index.model.ProcessInstanceState;

import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector;

import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_PROCESSINSTANCES_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_USERTASKINSTANCES_EVENTS;
import static org.kie.kogito.index.test.TestUtils.getProcessCloudEvent;
import static org.kie.kogito.index.test.TestUtils.getUserTaskCloudEvent;

public abstract class AbstractDomainMessagingHttpConsumerIT extends AbstractDomainMessagingConsumerIT {

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

    protected abstract String getTestProtobufFileContent() throws Exception;
}
