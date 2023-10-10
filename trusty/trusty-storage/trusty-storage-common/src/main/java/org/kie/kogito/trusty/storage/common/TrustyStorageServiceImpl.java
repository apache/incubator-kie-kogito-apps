package org.kie.kogito.trusty.storage.common;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.explainability.api.CounterfactualExplainabilityRequest;
import org.kie.kogito.explainability.api.CounterfactualExplainabilityResult;
import org.kie.kogito.explainability.api.LIMEExplainabilityResult;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.trusty.storage.api.model.ModelMetadata;
import org.kie.kogito.trusty.storage.api.model.ModelWithMetadata;
import org.kie.kogito.trusty.storage.api.model.decision.Decision;

@ApplicationScoped
public class TrustyStorageServiceImpl implements TrustyStorageService {

    @Inject
    StorageService storageService;

    @Override
    public Storage<String, Decision> getDecisionsStorage() {
        return storageService.getCache(DECISIONS_STORAGE, Decision.class);
    }

    @Override
    public Storage<String, LIMEExplainabilityResult> getLIMEResultStorage() {
        return storageService.getCache(LIME_RESULTS_STORAGE, LIMEExplainabilityResult.class);
    }

    @Override
    public <T extends ModelMetadata, E extends ModelWithMetadata<T>> Storage<String, E> getModelStorage(Class<E> modelWithMetadata) {
        return storageService.getCache(MODELS_STORAGE, modelWithMetadata);
    }

    @Override
    public Storage<String, CounterfactualExplainabilityRequest> getCounterfactualRequestStorage() {
        return storageService.getCache(COUNTERFACTUAL_REQUESTS_STORAGE, CounterfactualExplainabilityRequest.class);
    }

    @Override
    public Storage<String, CounterfactualExplainabilityResult> getCounterfactualResultStorage() {
        return storageService.getCache(COUNTERFACTUAL_RESULTS_STORAGE, CounterfactualExplainabilityResult.class);
    }
}
