package org.kie.kogito.trusty.storage.postgresql;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.explainability.api.CounterfactualExplainabilityResult;
import org.kie.kogito.persistence.postgresql.model.CacheEntityRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.kie.kogito.trusty.storage.common.TrustyStorageService.COUNTERFACTUAL_RESULTS_STORAGE;

@ApplicationScoped
public class CounterfactualResultsStorage extends BaseTransactionalStorage<CounterfactualExplainabilityResult> {

    CounterfactualResultsStorage() {
        //CDI proxy
    }

    @Inject
    public CounterfactualResultsStorage(CacheEntityRepository repository, ObjectMapper mapper) {
        super(COUNTERFACTUAL_RESULTS_STORAGE, repository, mapper, CounterfactualExplainabilityResult.class);
    }
}
