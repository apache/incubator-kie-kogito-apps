package org.kie.kogito.trusty.storage.api;

import org.kie.kogito.storage.api.Storage;
import org.kie.kogito.trusty.storage.api.model.Execution;

public interface TrustyStorageService {

    Storage<String, Execution> getExecutionStorage();
}
