package org.kie.kogito.index.redis;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.kogito.index.DataIndexStorageService;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.schema.SchemaRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class RedisProcessIndexObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisProcessIndexObserver.class);

    @Inject
    DataIndexStorageService dataIndexStorageService;

    @Inject
    IndexSchemaAcceptor indexSchemaAcceptor;

    public void onSchemaRegisteredEvent(@Observes SchemaRegisteredEvent event) {
        if (indexSchemaAcceptor.accept(event.getSchemaType())) {
            LOGGER.info(event.getSchemaDescriptor().getName());
            //            indexes.putAll(event.getSchemaDescriptor().getEntityIndexDescriptors());
//            updateIndexes(event.getSchemaDescriptor().getEntityIndexDescriptors().values());
//
//            event.getSchemaDescriptor().getProcessDescriptor().ifPresent(processDescriptor -> processIndexEvent.fire(new ProcessIndexEvent(processDescriptor)));
        }
    }

}
