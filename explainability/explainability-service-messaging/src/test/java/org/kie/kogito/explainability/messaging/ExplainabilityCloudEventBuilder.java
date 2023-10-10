package org.kie.kogito.explainability.messaging;

import java.net.URI;

import org.kie.kogito.event.cloudevents.utils.CloudEventUtils;
import org.kie.kogito.explainability.api.BaseExplainabilityRequest;

import io.cloudevents.CloudEvent;

public class ExplainabilityCloudEventBuilder {

    public static CloudEvent buildCloudEvent(BaseExplainabilityRequest request) {
        return CloudEventUtils.build(
                request.getExecutionId(),
                URI.create("trustyService/test"),
                request,
                BaseExplainabilityRequest.class).get();
    }

    public static String buildCloudEventJsonString(BaseExplainabilityRequest request) {
        return CloudEventUtils.encode(buildCloudEvent(request)).orElseThrow(IllegalStateException::new);
    }
}
