package org.kie.kogito.explainability;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.model.PredictionProvider;

import io.vertx.mutiny.core.Vertx;

import static org.kie.kogito.explainability.TestUtils.LIME_REQUEST;

class PredictionProviderFactoryImplTest {

    @Test
    void createPredictionProvider() {
        PredictionProviderFactoryImpl factory = new PredictionProviderFactoryImpl(
                Vertx.vertx(),
                ThreadContext.builder().build(),
                ManagedExecutor.builder().build());
        PredictionProvider predictionProvider = factory.createPredictionProvider(LIME_REQUEST.getServiceUrl(),
                LIME_REQUEST.getModelIdentifier(),
                LIME_REQUEST.getOutputs());
        Assertions.assertNotNull(predictionProvider);
        Assertions.assertTrue(predictionProvider instanceof RemotePredictionProvider);
    }
}
