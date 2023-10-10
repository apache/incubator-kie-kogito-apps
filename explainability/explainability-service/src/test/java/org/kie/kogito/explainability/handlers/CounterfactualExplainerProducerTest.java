package org.kie.kogito.explainability.handlers;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.local.counterfactual.CounterfactualExplainer;

import io.smallrye.context.SmallRyeManagedExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CounterfactualExplainerProducerTest {

    @Test
    void produce() {
        final ManagedExecutor executor = SmallRyeManagedExecutor.builder().build();
        CounterfactualExplainerProducer producer = new CounterfactualExplainerProducer(0.01, executor);
        CounterfactualExplainer counterfactualExplainer = producer.produce();

        assertNotNull(counterfactualExplainer);
        assertEquals(0.01, counterfactualExplainer.getCounterfactualConfig().getGoalThreshold());
        assertEquals(executor, counterfactualExplainer.getCounterfactualConfig().getExecutor());
    }

}
