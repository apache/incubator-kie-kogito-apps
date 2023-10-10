package org.kie.kogito.trusty.storage.postgresql;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.explainability.api.LIMEExplainabilityResult;
import org.kie.kogito.persistence.postgresql.model.CacheEntityRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.kie.kogito.trusty.storage.common.TrustyStorageService.LIME_RESULTS_STORAGE;

@ApplicationScoped
public class LIMEResultsStorage extends BaseTransactionalStorage<LIMEExplainabilityResult> {

    LIMEResultsStorage() {
        //CDI proxy
    }

    @Inject
    public LIMEResultsStorage(CacheEntityRepository repository, ObjectMapper mapper) {
        super(LIME_RESULTS_STORAGE, repository, mapper, LIMEExplainabilityResult.class);
    }
}
