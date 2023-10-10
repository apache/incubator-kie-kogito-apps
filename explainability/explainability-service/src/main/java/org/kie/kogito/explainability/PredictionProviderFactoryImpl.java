package org.kie.kogito.explainability;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.kie.kogito.explainability.api.HasNameValue;
import org.kie.kogito.explainability.api.ModelIdentifier;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.tracing.typedvalue.TypedValue;

import io.vertx.mutiny.core.Vertx;

@ApplicationScoped
public class PredictionProviderFactoryImpl implements PredictionProviderFactory {

    private final Vertx vertx;
    private final ThreadContext threadContext;
    private final ManagedExecutor managedExecutor;

    @Inject
    public PredictionProviderFactoryImpl(
            Vertx vertx,
            ThreadContext threadContext,
            ManagedExecutor managedExecutor) {

        this.vertx = vertx;
        this.threadContext = threadContext;
        this.managedExecutor = managedExecutor;
    }

    @Override
    public PredictionProvider createPredictionProvider(String serviceUrl,
            ModelIdentifier modelIdentifier,
            Collection<? extends HasNameValue<TypedValue>> predictionOutputs) {
        return new RemotePredictionProvider(serviceUrl,
                modelIdentifier,
                predictionOutputs,
                vertx,
                threadContext,
                managedExecutor);
    }
}
