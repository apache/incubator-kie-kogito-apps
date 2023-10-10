package org.kie.kogito.explainability.rest;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;

public class PredictionProviderMock implements PredictionProvider {

    @Override
    public CompletableFuture<List<PredictionOutput>> predictAsync(List<PredictionInput> inputs) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
