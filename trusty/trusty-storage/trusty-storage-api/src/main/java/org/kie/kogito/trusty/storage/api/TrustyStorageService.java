package org.kie.kogito.trusty.storage.api;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.trusty.storage.api.model.Decision;

public interface TrustyStorageService {

    /**
     * Gets the decision storage.
     *
     * @return The Storage for decisions.
     */
    Storage<String, Decision> getDecisionsStorage();
}
