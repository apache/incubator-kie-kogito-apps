package org.kie.kogito.explainability;

import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

import org.kie.kogito.explainability.api.BaseExplainabilityRequest;
import org.kie.kogito.explainability.api.BaseExplainabilityResult;

public interface ExplanationService {

    CompletionStage<BaseExplainabilityResult> explainAsync(
            BaseExplainabilityRequest request);

    CompletionStage<BaseExplainabilityResult> explainAsync(
            BaseExplainabilityRequest request,
            Consumer<BaseExplainabilityResult> intermediateResultConsumer);

}
