package org.kie.kogito.trusty.service.common.handlers;

import org.kie.kogito.explainability.api.BaseExplainabilityResult;
import org.kie.kogito.persistence.api.Storage;

public interface ExplainabilityResultsManager<R extends BaseExplainabilityResult> {

    /**
     * Purge the results storage of any unwanted entries.
     *
     * @param counterfactualId The counterfactual request Id.
     * @param storage The results storage.
     */
    void purge(String counterfactualId, Storage<String, R> storage);
}
