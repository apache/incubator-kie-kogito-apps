package org.kie.kogito.explainability.handlers;

import java.security.SecureRandom;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.local.lime.optim.RecordingLimeExplainer;
import org.kie.kogito.explainability.model.PerturbationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class LimeExplainerProducer {

    private static final Logger LOG = LoggerFactory.getLogger(LimeExplainerProducer.class);

    private final Integer numberOfSamples;
    private final Integer numberOfPerturbations;
    private final Integer recordedPredictions;

    @Inject
    public LimeExplainerProducer(
            @ConfigProperty(name = "trusty.explainability.numberOfSamples", defaultValue = "100") Integer numberOfSamples,
            @ConfigProperty(name = "trusty.explainability.numberOfPerturbations", defaultValue = "1") Integer numberOfPerturbations,
            @ConfigProperty(name = "trusty.explainability.recordedPredictions", defaultValue = "10") Integer recordedPredictions) {
        this.numberOfSamples = numberOfSamples;
        this.numberOfPerturbations = numberOfPerturbations;
        this.recordedPredictions = recordedPredictions;
    }

    @Produces
    public LimeExplainer produce() {
        LOG.debug("LimeExplainer created (numberOfSamples={}, numberOfPerturbations={})", numberOfSamples, numberOfPerturbations);
        LimeConfig limeConfig = new LimeConfig()
                .withSamples(numberOfSamples)
                .withPerturbationContext(new PerturbationContext(new SecureRandom(), numberOfPerturbations));
        return new RecordingLimeExplainer(limeConfig, recordedPredictions);
    }
}
