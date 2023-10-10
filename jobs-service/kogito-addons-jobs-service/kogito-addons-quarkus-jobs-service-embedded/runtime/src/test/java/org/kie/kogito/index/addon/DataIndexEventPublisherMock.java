package org.kie.kogito.index.addon;

import java.util.Collection;

import org.kie.kogito.event.DataEvent;
import org.kie.kogito.event.EventPublisher;

/**
 * Mock class used by the org.kie.kogito.addons.quarkus.jobs.service.embedded.stream.EventPublisherJobStreamsTest.
 */
public class DataIndexEventPublisherMock implements EventPublisher {

    @Override
    public void publish(DataEvent<?> event) {
        // this method is por testing purposes, no code is required here.
    }

    @Override
    public void publish(Collection<DataEvent<?>> events) {
        // this method is por testing purposes, no code is required here.
    }
}
