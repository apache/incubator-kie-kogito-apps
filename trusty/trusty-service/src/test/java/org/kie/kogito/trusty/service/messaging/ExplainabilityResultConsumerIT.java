package org.kie.kogito.trusty.service.messaging;

import java.net.URI;

import io.cloudevents.v1.CloudEventImpl;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;
import org.kie.kogito.tracing.decision.event.CloudEventUtils;
import org.kie.kogito.trusty.service.TrustyKafkaTestResource;
import org.kie.kogito.trusty.service.TrustyService;
import org.kie.kogito.trusty.storage.api.model.ExplainabilityResult;

import static org.kie.kogito.trusty.service.messaging.KafkaUtils.generateProducer;
import static org.kie.kogito.trusty.service.messaging.KafkaUtils.sendToKafkaAndWaitForCompletion;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@QuarkusTest
@QuarkusTestResource(TrustyKafkaTestResource.class)
public class ExplainabilityResultConsumerIT {

    private static final String TOPIC = "trusty-explainability-result-test";

    @InjectMock
    TrustyService trustyService;

    KafkaProducer<String, String> producer;

    @BeforeEach
    public void setup() {
        producer = generateProducer();
    }

    @Test
    public void explainabilityResultIsProcessedAndStored() throws Exception {
        String executionId = "executionId";

        doNothing().when(trustyService).storeExplainability(eq(executionId), any(ExplainabilityResult.class));

        sendToKafkaAndWaitForCompletion(buildCloudEventJsonString(new ExplainabilityResultDto(executionId)), producer, TOPIC);

        verify(trustyService, times(1)).storeExplainability(any(String.class), any(ExplainabilityResult.class));
    }

    public static CloudEventImpl<ExplainabilityResultDto> buildExplainabilityCloudEvent(ExplainabilityResultDto resultDto) {
        return CloudEventUtils.build(
                resultDto.getExecutionId(),
                URI.create("explainabilityResult/test"),
                resultDto,
                ExplainabilityResultDto.class
        );
    }

    public static String buildCloudEventJsonString(ExplainabilityResultDto resultDto) {
        return CloudEventUtils.encode(buildExplainabilityCloudEvent(resultDto));
    }
}
