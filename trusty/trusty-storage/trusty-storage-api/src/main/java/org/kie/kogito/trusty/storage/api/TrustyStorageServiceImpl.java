package org.kie.kogito.trusty.storage.api;

import javax.inject.Inject;

import org.kie.kogito.storage.api.Storage;
import org.kie.kogito.storage.api.StorageService;
import org.kie.kogito.trusty.storage.api.model.Execution;

public class TrustyStorageServiceImpl implements TrustyStorageService {
    private static final String EXECUTIONS_STORAGE = "executions";

    @Inject
    StorageService storageService;

    public Storage<String, Execution> getExecutionStorage(){
        return storageService.getCache(EXECUTIONS_STORAGE, Execution.class);
    }
}
