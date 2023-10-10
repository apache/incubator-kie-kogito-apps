package org.kie.kogito.explainability;

import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.explainability.api.BaseExplainabilityRequest;
import org.kie.kogito.explainability.api.BaseExplainabilityResult;
import org.kie.kogito.explainability.handlers.LocalExplainerServiceHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ExplanationServiceImpl implements ExplanationService {

    private static final Logger LOG = LoggerFactory.getLogger(ExplanationServiceImpl.class);

    private final LocalExplainerServiceHandlerRegistry explainerServiceHandlerRegistry;

    @Inject
    public ExplanationServiceImpl(LocalExplainerServiceHandlerRegistry explainerServiceHandlerRegistry) {
        this.explainerServiceHandlerRegistry = explainerServiceHandlerRegistry;
    }

    @Override
    public CompletionStage<BaseExplainabilityResult> explainAsync(BaseExplainabilityRequest request) {
        return explainAsync(request,
                baseExplainabilityResult -> {
                    /* NOP */});
    }

    @Override
    public CompletionStage<BaseExplainabilityResult> explainAsync(BaseExplainabilityRequest request,
            Consumer<BaseExplainabilityResult> intermediateResultConsumer) {
        LOG.debug("Explainability request {} with executionId {} for model {}:{}",
                request.getClass().getSimpleName(),
                request.getExecutionId(),
                request.getModelIdentifier().getResourceType(),
                request.getModelIdentifier().getResourceId());

        return explainerServiceHandlerRegistry.explainAsyncWithResults(request, intermediateResultConsumer);
    }
}
