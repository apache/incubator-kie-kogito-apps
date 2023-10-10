package org.kie.kogito.trusty.service.common.handlers;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.kie.kogito.explainability.api.BaseExplainabilityResult;

@ApplicationScoped
public class ExplainerServiceHandlerRegistry {

    private Instance<ExplainerServiceHandler<?>> explanationHandlers;

    protected ExplainerServiceHandlerRegistry() {
        //CDI proxy
    }

    @Inject
    public ExplainerServiceHandlerRegistry(@Any Instance<ExplainerServiceHandler<?>> explanationHandlers) {
        this.explanationHandlers = explanationHandlers;
    }

    /**
     * Gets the result for an explanation.
     *
     * @param executionId The execution Id for which to retrieve an explanation result.
     * @param type The type of result to retrieve.
     * @return The result of an explanation.
     */
    public <T extends BaseExplainabilityResult> T getExplainabilityResultById(String executionId, Class<T> type) {
        ExplainerServiceHandler<?> explanationHandler = getLocalExplainer(type)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Explainability result for '%s' is not supported", type.getName())));

        return cast(explanationHandler.getExplainabilityResultById(executionId));
    }

    /**
     * Stores the result for an explanation.
     *
     * @param executionId The execution Id for which to retrieve an explanation result.
     * @param result The result to store.
     */
    public <T extends BaseExplainabilityResult> void storeExplainabilityResult(String executionId, T result) {
        T type = cast(result);
        ExplainerServiceHandler<?> explanationHandler =
                getLocalExplainer(type.getClass())
                        .orElseThrow(() -> new IllegalArgumentException(String.format("Explainability result for '%s' is not supported", type.getClass().getName())));

        explanationHandler.storeExplainabilityResult(executionId, cast(result));
    }

    private <T extends BaseExplainabilityResult> Optional<ExplainerServiceHandler<?>> getLocalExplainer(Class<T> type) {
        return this.explanationHandlers.stream().filter(handler -> handler.supports(type)).findFirst();
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseExplainabilityResult> T cast(BaseExplainabilityResult type) {
        return (T) type;
    }
}
