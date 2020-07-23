package org.kie.kogito.trusty.service.messaging;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.service.TrustyKafkaTestResource;
import org.kie.kogito.trusty.service.TrustyService;
import org.kie.kogito.trusty.storage.api.model.Decision;

import static org.kie.kogito.trusty.service.TrustyServiceTestUtils.buildCloudEventJsonString;
import static org.kie.kogito.trusty.service.TrustyServiceTestUtils.buildCorrectTraceEvent;
import static org.kie.kogito.trusty.service.messaging.KafkaUtils.generateProducer;
import static org.kie.kogito.trusty.service.messaging.KafkaUtils.sendToKafkaAndWaitForCompletion;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@QuarkusTest
@QuarkusTestResource(TrustyKafkaTestResource.class)
public class TraceEventConsumerIT {

    @InjectMock
    TrustyService trustyService;

    KafkaProducer<String, String> producer;

    @BeforeEach
    public void setup() {
        producer = generateProducer();
    }

    @Test
    public void eventLoopIsNotStoppedWithException() throws Exception {
        String executionIdException = "idException";
        String executionIdNoException = "idNoException";
        doThrow(new RuntimeException("Something really bad")).when(trustyService).storeDecision(eq(executionIdException), any(Decision.class));
        doNothing().when(trustyService).storeDecision(eq(executionIdNoException), any(Decision.class));

        sendToKafkaAndWaitForCompletion(buildCloudEventJsonString(buildCorrectTraceEvent(executionIdException)), producer);
        sendToKafkaAndWaitForCompletion(buildCloudEventJsonString(buildCorrectTraceEvent(executionIdNoException)), producer);

        verify(trustyService, times(2)).storeDecision(any(String.class), any(Decision.class));
    }
}
