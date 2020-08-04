package org.kie.kogito.explainability;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import io.cloudevents.v1.CloudEventImpl;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.ExplainabilityRequestDto;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;
import org.kie.kogito.explainability.models.ExplainabilityRequest;
import org.kie.kogito.messaging.commons.KafkaTestResource;
import org.kie.kogito.tracing.decision.event.CloudEventUtils;

import static org.kie.kogito.messaging.commons.KafkaUtils.generateProducer;
import static org.kie.kogito.messaging.commons.KafkaUtils.sendToKafkaAndWaitForCompletion;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class ExplainabilityMessagingHandlerIT {

    private static final String TOPIC = "trusty-explainability-request-test";

    @InjectMock
    IExplanationService explanationService;

    KafkaProducer<String, String> producer;

    @BeforeEach
    public void setup() {
        producer = generateProducer();
    }

    @Test
    public void eventLoopIsNotStoppedWithException() throws Exception {
        String executionId = "idException";
        ExplainabilityRequestDto request = new ExplainabilityRequestDto(executionId);
        when(explanationService.explainAsync(any(ExplainabilityRequest.class))).thenReturn(CompletableFuture.completedFuture(new ExplainabilityResultDto(executionId)));

        sendToKafkaAndWaitForCompletion(buildCloudEventJsonString(request), producer, TOPIC);

        verify(explanationService, times(1)).explainAsync(any(ExplainabilityRequest.class));
    }

    private CloudEventImpl<ExplainabilityRequestDto> buildCloudEvent(ExplainabilityRequestDto request) {
        return CloudEventUtils.build(
                request.getExecutionId(),
                URI.create("trustyService/test"),
                request,
                ExplainabilityRequestDto.class
        );
    }

    public String buildCloudEventJsonString(ExplainabilityRequestDto request) {
        return CloudEventUtils.encode(buildCloudEvent(request));
    }

}
