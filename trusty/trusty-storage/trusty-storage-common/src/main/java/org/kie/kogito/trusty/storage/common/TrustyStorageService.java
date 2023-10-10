package org.kie.kogito.trusty.storage.common;

import org.kie.kogito.explainability.api.CounterfactualExplainabilityRequest;
import org.kie.kogito.explainability.api.CounterfactualExplainabilityResult;
import org.kie.kogito.explainability.api.LIMEExplainabilityResult;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.trusty.storage.api.model.ModelMetadata;
import org.kie.kogito.trusty.storage.api.model.ModelWithMetadata;
import org.kie.kogito.trusty.storage.api.model.decision.Decision;

public interface TrustyStorageService {

    String DECISIONS_STORAGE = "decisions";
    String LIME_RESULTS_STORAGE = "limeResults";
    String COUNTERFACTUAL_REQUESTS_STORAGE = "counterfactualRequests";
    String COUNTERFACTUAL_RESULTS_STORAGE = "counterfactualResults";
    String MODELS_STORAGE = "models";

    /**
     * Gets the decision storage.
     *
     * @return The Storage for decisions.
     */
    Storage<String, Decision> getDecisionsStorage();

    /**
     * Gets the LIME results storage.
     *
     * @return The Storage for LIME explainability results.
     */
    Storage<String, LIMEExplainabilityResult> getLIMEResultStorage();

    /**
     * Gets the model definition storage.
     *
     * @return The Storage for model definitions.
     */
    <T extends ModelMetadata, E extends ModelWithMetadata<T>> Storage<String, E> getModelStorage(Class<E> modelWithMetadata);

    /**
     * Gets the Counterfactual requests storage.
     *
     * @return The Storage for Counterfactual explainability requests.
     */
    Storage<String, CounterfactualExplainabilityRequest> getCounterfactualRequestStorage();

    /**
     * Gets the Counterfactual results storage.
     *
     * @return The Storage for Counterfactual explainability results.
     */
    Storage<String, CounterfactualExplainabilityResult> getCounterfactualResultStorage();
}
