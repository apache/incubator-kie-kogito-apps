package org.kie.kogito.index.mongodb.storage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.kogito.index.storage.DataIndexStorageService;
import org.kie.kogito.persistence.mongodb.index.IndexCreateOrUpdateEvent;
import org.kie.kogito.persistence.mongodb.index.ProcessIndexEvent;

import static org.kie.kogito.index.mongodb.Constants.getDomainCollectionName;

@ApplicationScoped
public class ProcessIndexObserver {

    @Inject
    DataIndexStorageService dataIndexStorageService;

    @Inject
    Event<IndexCreateOrUpdateEvent> indexCreateOrUpdateEvent;

    public void onProcessIndexEvent(@Observes ProcessIndexEvent event) {
        String processId = event.getProcessDescriptor().getProcessId();
        String processType = event.getProcessDescriptor().getProcessType();
        dataIndexStorageService.getProcessIdModelCache().put(processId, processType);

        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(getDomainCollectionName(processId), processType));
    }
}
