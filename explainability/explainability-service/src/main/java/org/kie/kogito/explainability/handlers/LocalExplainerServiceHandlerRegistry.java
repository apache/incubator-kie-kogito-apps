package org.kie.kogito.explainability.handlers;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.kie.kogito.explainability.api.BaseExplainabilityRequest;
import org.kie.kogito.explainability.api.BaseExplainabilityResult;
import org.kie.kogito.explainability.local.LocalExplainer;

@ApplicationScoped
public class LocalExplainerServiceHandlerRegistry {

    public static final String SERVICE_HANDLER_NOT_FOUND_ERROR_MESSAGE = "LocalExplainerServiceHandler could not be found for '%s'";

    private Instance<LocalExplainerServiceHandler<?, ?>> explanationHandlers;

    protected LocalExplainerServiceHandlerRegistry() {
        //CDI proxy
    }

    @Inject
    public LocalExplainerServiceHandlerRegistry(@Any Instance<LocalExplainerServiceHandler<?, ?>> explanationHandlers) {
        this.explanationHandlers = explanationHandlers;
    }

    /**
     * Requests calculation of an explanation decorated with both "success" and "failure" result handlers.
     * See:
     * - {@link LocalExplainer#explainAsync}
     * - {@link LocalExplainerServiceHandler#createSucceededResult(BaseExplainabilityRequest, Object)}
     * - {@link LocalExplainerServiceHandler#createFailedResult(BaseExplainabilityRequest, Throwable)}
     *
     * @param request The explanation request.
     * @param intermediateResultsConsumer A consumer of intermediate results provided by the explainer.
     * @return
     */
    public <R extends BaseExplainabilityRequest, S extends BaseExplainabilityResult> CompletableFuture<BaseExplainabilityResult> explainAsyncWithResults(R request,
            Consumer<S> intermediateResultsConsumer) {

        LocalExplainerServiceHandler<?, ?> explanationHandler = getLocalExplainer(request.getClass())
                .orElseThrow(() -> new IllegalArgumentException(String.format(SERVICE_HANDLER_NOT_FOUND_ERROR_MESSAGE, request.getClass().getName())));

        try {
            return explanationHandler.explainAsyncWithResults(cast(request),
                    castConsumer(intermediateResultsConsumer));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(explanationHandler.createFailedResult(cast(request), e));
        }
    }

    private <T extends BaseExplainabilityRequest> Optional<LocalExplainerServiceHandler<?, ?>> getLocalExplainer(Class<T> type) {
        return this.explanationHandlers.stream().filter(explainer -> explainer.supports(type)).findFirst();
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseExplainabilityRequest> T cast(BaseExplainabilityRequest type) {
        return (T) type;
    }

    @SuppressWarnings("unchecked")
    private <S extends BaseExplainabilityResult> Consumer<S> castConsumer(Consumer<?> consumer) {
        return (Consumer<S>) consumer;
    }

}
