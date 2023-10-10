package org.kie.kogito.trusty.service.common.messaging.outgoing;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.LIMEExplainabilityRequest;
import org.kie.kogito.explainability.api.ModelIdentifier;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExplainabilityRequestProducerTest {

    @Test
    void test() {
        AssertSubscriber<String> subscriber = AssertSubscriber.create(1);

        ExplainabilityRequestProducer producer = new ExplainabilityRequestProducer();
        producer.getEventPublisher().subscribe(subscriber);

        producer.sendEvent(new LIMEExplainabilityRequest(
                "executionId", "http://localhost:8080/model",
                new ModelIdentifier("dmn", "modelNamespace:model"),
                Collections.emptyList(),
                Collections.emptyList()));

        assertEquals(1, subscriber.getItems().size());
    }
}
