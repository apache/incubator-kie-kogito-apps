package org.kie.kogito.trusty.storage.postgresql;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.explainability.api.CounterfactualExplainabilityRequest;
import org.kie.kogito.persistence.postgresql.model.CacheEntityRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.kie.kogito.trusty.storage.common.TrustyStorageService.COUNTERFACTUAL_REQUESTS_STORAGE;

@ApplicationScoped
public class CounterfactualRequestsStorage extends BaseTransactionalStorage<CounterfactualExplainabilityRequest> {

    CounterfactualRequestsStorage() {
        //CDI proxy
    }

    @Inject
    public CounterfactualRequestsStorage(CacheEntityRepository repository, ObjectMapper mapper) {
        super(COUNTERFACTUAL_REQUESTS_STORAGE, repository, mapper, CounterfactualExplainabilityRequest.class);
    }
}
