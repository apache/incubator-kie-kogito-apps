package org.kie.kogito.explainability.handlers;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LimeExplainerProducerTest {

    @Test
    void produce() {
        LimeExplainerProducer producer = new LimeExplainerProducer(1, 2, 10);
        LimeExplainer limeExplainer = producer.produce();

        assertNotNull(limeExplainer);
        assertEquals(1, limeExplainer.getLimeConfig().getNoOfSamples());
        assertEquals(2, limeExplainer.getLimeConfig().getPerturbationContext().getNoOfPerturbations());
        assertEquals(LimeConfig.DEFAULT_NO_OF_RETRIES, limeExplainer.getLimeConfig().getNoOfRetries());
    }
}
