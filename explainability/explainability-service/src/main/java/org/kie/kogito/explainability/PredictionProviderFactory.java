package org.kie.kogito.explainability;

import java.util.Collection;

import org.kie.kogito.explainability.api.HasNameValue;
import org.kie.kogito.explainability.api.ModelIdentifier;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.tracing.typedvalue.TypedValue;

public interface PredictionProviderFactory {

    PredictionProvider createPredictionProvider(String serviceUrl,
            ModelIdentifier modelIdentifier,
            Collection<? extends HasNameValue<TypedValue>> predictionOutputs);
}
