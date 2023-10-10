package org.kie.kogito.explainability.rest;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.explainability.PredictionProviderFactory;
import org.kie.kogito.explainability.api.HasNameValue;
import org.kie.kogito.explainability.api.ModelIdentifier;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.tracing.typedvalue.TypedValue;

import io.quarkus.test.Mock;

@Mock
@ApplicationScoped
public class PredictionProviderFactoryMock implements PredictionProviderFactory {

    @Override
    public PredictionProvider createPredictionProvider(String serviceUrl,
            ModelIdentifier modelIdentifier,
            Collection<? extends HasNameValue<TypedValue>> predictionOutputs) {
        return new PredictionProviderMock();
    }
}
