package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.CounterfactualDomainRange;
import org.kie.kogito.explainability.api.CounterfactualExplainabilityRequest;
import org.kie.kogito.explainability.api.CounterfactualSearchDomain;
import org.kie.kogito.explainability.api.CounterfactualSearchDomainUnitValue;
import org.kie.kogito.explainability.api.ModelIdentifier;
import org.kie.kogito.explainability.api.NamedTypedValue;
import org.kie.kogito.tracing.typedvalue.UnitValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class CounterfactualExplainabilityRequestMarshallerTest extends MarshallerTestTemplate {

    @Test
    public void testWriteAndRead() throws IOException {
        ModelIdentifier modelIdentifier = new ModelIdentifier("resourceType", "resourceId");
        List<NamedTypedValue> originalInputs = Collections.singletonList(new NamedTypedValue("unitIn",
                new UnitValue("number",
                        "number",
                        JsonNodeFactory.instance.numberNode(10))));

        List<NamedTypedValue> goals = Collections.singletonList(new NamedTypedValue("unitIn",
                new UnitValue("number",
                        "number",
                        JsonNodeFactory.instance.numberNode(10))));
        List<CounterfactualSearchDomain> searchDomains = Collections.singletonList(
                new CounterfactualSearchDomain("age",
                        new CounterfactualSearchDomainUnitValue("integer",
                                "integer",
                                Boolean.TRUE,
                                new CounterfactualDomainRange(JsonNodeFactory.instance.numberNode(0), JsonNodeFactory.instance.numberNode(10)))));

        CounterfactualExplainabilityRequest request = new CounterfactualExplainabilityRequest("executionId",
                "serviceUrl",
                modelIdentifier,
                "counterfactualId",
                originalInputs,
                goals,
                searchDomains, 60L);
        CounterfactualExplainabilityRequestMarshaller marshaller = new CounterfactualExplainabilityRequestMarshaller(new ObjectMapper());

        marshaller.writeTo(writer, request);
        CounterfactualExplainabilityRequest retrieved = marshaller.readFrom(reader);

        Assertions.assertEquals(request.getExecutionId(), retrieved.getExecutionId());
        Assertions.assertEquals(request.getCounterfactualId(), retrieved.getCounterfactualId());
        Assertions.assertEquals(goals.get(0).getName(), retrieved.getGoals().stream().findFirst().get().getName());
        Assertions.assertEquals(searchDomains.get(0).getName(), retrieved.getSearchDomains().stream().findFirst().get().getName());
        Assertions.assertEquals(0, ((CounterfactualDomainRange) retrieved.getSearchDomains().stream().findFirst().get().getValue().toUnit().getDomain()).getLowerBound().asInt());
        Assertions.assertEquals(60L, request.getMaxRunningTimeSeconds());
    }
}
