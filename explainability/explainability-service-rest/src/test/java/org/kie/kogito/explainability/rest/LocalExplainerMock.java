package org.kie.kogito.explainability.rest;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.explainability.local.LocalExplainer;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.Saliency;

import io.quarkus.test.Mock;

import static java.util.Collections.emptyMap;

@Mock
@ApplicationScoped
public class LocalExplainerMock implements LocalExplainer<Map<String, Saliency>> {

    @Override
    public CompletableFuture<Map<String, Saliency>> explainAsync(Prediction prediction,
            PredictionProvider model,
            Consumer<Map<String, Saliency>> intermediateResultsConsumer) {
        return CompletableFuture.completedFuture(emptyMap());
    }
}
