package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.kogito.explainability.api.NamedTypedValue;
import org.kie.kogito.tracing.typedvalue.UnitValue;
import org.kie.kogito.trusty.storage.api.model.decision.Decision;
import org.kie.kogito.trusty.storage.api.model.decision.DecisionInput;
import org.kie.kogito.trusty.storage.api.model.decision.DecisionOutcome;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class DecisionMarshallerTest extends MarshallerTestTemplate {

    @Test
    public void testWriteAndRead() throws IOException {
        List<DecisionInput> inputs = Collections.singletonList(new DecisionInput("id", "in", new UnitValue("nameIn", "number", JsonNodeFactory.instance.numberNode(10))));
        List<DecisionOutcome> outcomes = Collections.singletonList(new DecisionOutcome("id", "out",
                DMNDecisionResult.DecisionEvaluationStatus.SUCCEEDED.toString(),
                new UnitValue("nameOut", "number", JsonNodeFactory.instance.numberNode(10)),
                List.of(new NamedTypedValue("nameOut", new UnitValue("number", "number", JsonNodeFactory.instance.numberNode(10)))),
                new ArrayList<>()));
        Decision decision = new Decision("executionId", "source", "serviceUrl", 0L, true, "executor", "model", "namespace", inputs, outcomes);
        DecisionMarshaller marshaller = new DecisionMarshaller(new ObjectMapper());

        marshaller.writeTo(writer, decision);
        Decision retrieved = marshaller.readFrom(reader);

        Assertions.assertEquals(decision.getExecutionId(), retrieved.getExecutionId());
        Assertions.assertEquals(decision.getSourceUrl(), retrieved.getSourceUrl());
        Assertions.assertEquals(decision.getServiceUrl(), retrieved.getServiceUrl());
        Assertions.assertEquals(decision.getExecutedModelName(), retrieved.getExecutedModelName());
        Assertions.assertEquals(inputs.get(0).getName(), retrieved.getInputs().stream().findFirst().get().getName());
        Assertions.assertEquals(inputs.get(0).getValue().getType(), retrieved.getInputs().stream().findFirst().get().getValue().getType());
        Assertions.assertEquals(inputs.get(0).getValue().toUnit().getBaseType(), retrieved.getInputs().stream().findFirst().get().getValue().toUnit().getBaseType());
        Assertions.assertEquals(outcomes.get(0).getOutcomeId(), retrieved.getOutcomes().stream().findFirst().get().getOutcomeId());
        Assertions.assertTrue(retrieved.getOutcomes().stream().findFirst().get().getMessages().isEmpty());
    }
}
