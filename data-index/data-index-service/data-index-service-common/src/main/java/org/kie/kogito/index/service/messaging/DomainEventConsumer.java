package org.kie.kogito.index.service.messaging;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.event.DataEvent;
import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.process.UserTaskInstanceDataEvent;
import org.kie.kogito.index.service.IndexingService;
import org.kie.kogito.index.service.json.ProcessInstanceMetaMapper;
import org.kie.kogito.index.service.json.UserTaskInstanceMetaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.arc.Lock;

import static java.lang.String.format;

@ApplicationScoped
@Lock
public class DomainEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainEventConsumer.class);

    @ConfigProperty(name = "kogito.data-index.domain-indexing", defaultValue = "true")
    Boolean indexDomain;

    @Inject
    IndexingService indexingService;

    public void onDomainEvent(@Observes DataEvent event) {
        if (!indexDomain) {
            return;
        }

        LOGGER.debug("Processing domain event: {}", event);
        indexingService.indexModel(getDomainData(event));
    }

    private ObjectNode getDomainData(DataEvent event) {
        if (event instanceof ProcessInstanceDataEvent) {
            return new ProcessInstanceMetaMapper().apply((ProcessInstanceDataEvent) event);
        }
        if (event instanceof UserTaskInstanceDataEvent) {
            return new UserTaskInstanceMetaMapper().apply((UserTaskInstanceDataEvent) event);
        }
        throw new IllegalArgumentException(
                format("Unknown message type: '%s' for event class: '%s'", event.getType(), event.getClass().getName()));
    }
}
