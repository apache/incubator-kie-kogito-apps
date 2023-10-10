package org.kie.kogito.explainability.messaging;

import java.util.Collections;
import java.util.function.Consumer;

import org.kie.kogito.explainability.api.BaseExplainabilityRequest;
import org.kie.kogito.explainability.api.BaseExplainabilityResult;
import org.kie.kogito.explainability.api.LIMEExplainabilityRequest;
import org.kie.kogito.explainability.api.LIMEExplainabilityResult;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(KafkaQuarkusTestResource.class)
class ExplainabilityMessagingHandlerLIMEIT extends BaseExplainabilityMessagingHandlerIT {

    @Override
    protected BaseExplainabilityRequest buildRequest() {
        return new LIMEExplainabilityRequest(EXECUTION_ID,
                SERVICE_URL,
                MODEL_IDENTIFIER,
                Collections.emptyList(),
                Collections.emptyList());
    }

    @Override
    protected BaseExplainabilityResult buildResult() {
        return LIMEExplainabilityResult.buildSucceeded(EXECUTION_ID, Collections.emptyList());
    }

    @Override
    protected void assertResult(BaseExplainabilityResult result) {
        assertTrue(result instanceof LIMEExplainabilityResult);
    }

    @Override
    protected int getTotalExpectedEventCountWithIntermediateResults() {
        return 1;
    }

    @Override
    protected void mockExplainAsyncInvocationWithIntermediateResults(Consumer<BaseExplainabilityResult> callback) {
        //LIME does not support intermediate results; so nothing to do!
    }
}
