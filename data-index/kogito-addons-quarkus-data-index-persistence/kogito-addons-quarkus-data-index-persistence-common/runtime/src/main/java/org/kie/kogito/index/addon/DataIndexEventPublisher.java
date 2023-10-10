package org.kie.kogito.index.addon;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.kie.kogito.event.DataEvent;
import org.kie.kogito.event.EventPublisher;
import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.process.UserTaskInstanceDataEvent;
import org.kie.kogito.index.event.ProcessInstanceEventMapper;
import org.kie.kogito.index.event.UserTaskInstanceEventMapper;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.service.IndexingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.kogito.index.json.JsonUtils.getObjectMapper;

@ApplicationScoped
public class DataIndexEventPublisher implements EventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexEventPublisher.class);

    @Inject
    IndexingService indexingService;

    @Override
    @Transactional
    public void publish(DataEvent<?> event) {
        LOGGER.debug("Sending event to embedded data index: {}", event);
        switch (event.getType()) {
            case "ProcessInstanceEvent":
                indexingService.indexProcessInstance(new ProcessInstanceEventMapper().apply((ProcessInstanceDataEvent) event));
                break;
            case "UserTaskInstanceEvent":
                indexingService.indexUserTaskInstance(new UserTaskInstanceEventMapper().apply((UserTaskInstanceDataEvent) event));
                break;
            case "JobEvent":
                try {
                    indexingService.indexJob(getObjectMapper().readValue(new String((byte[]) event.getData()), Job.class));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
                break;
            default:
                LOGGER.debug("Unknown type of event '{}', ignoring for this publisher", event.getType());
        }
    }

    @Override
    public void publish(Collection<DataEvent<?>> events) {
        events.forEach(this::publish);
    }

    protected void setIndexingService(IndexingService indexingService) {
        this.indexingService = indexingService;
    }
}
