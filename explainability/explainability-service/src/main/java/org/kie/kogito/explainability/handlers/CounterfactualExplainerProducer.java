package org.kie.kogito.explainability.handlers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.kie.kogito.explainability.local.counterfactual.CounterfactualConfig;
import org.kie.kogito.explainability.local.counterfactual.CounterfactualExplainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CounterfactualExplainerProducer {

    private static final Logger LOG = LoggerFactory.getLogger(CounterfactualExplainerProducer.class);

    private final Double goalThreshold;
    private final ManagedExecutor executor;

    @Inject
    public CounterfactualExplainerProducer(
            @ConfigProperty(name = "trusty.explainability.counterfactuals.goalThreshold",
                    defaultValue = "0.01") Double goalThreshold,
            ManagedExecutor executor) {
        this.goalThreshold = goalThreshold;
        this.executor = executor;
    }

    @Produces
    public CounterfactualExplainer produce() {
        LOG.debug("CounterfactualExplainer created");
        final CounterfactualConfig counterfactualConfig = new CounterfactualConfig()
                .withGoalThreshold(this.goalThreshold)
                .withExecutor(executor);
        return new CounterfactualExplainer(counterfactualConfig);
    }
}