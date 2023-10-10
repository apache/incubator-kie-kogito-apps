package org.kie.kogito.trusty.service.common.responses;

import java.util.Collection;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.service.common.responses.decision.DecisionStructuredInputsResponse;
import org.kie.kogito.trusty.service.common.responses.process.ProcessStructuredInputsResponse;
import org.kie.kogito.trusty.storage.api.model.Input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base abstract class for <b>StructuredInputsResponse</b>
 * 
 * @param <T>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "@type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DecisionStructuredInputsResponse.class, name = "DECISION"),
        @JsonSubTypes.Type(value = ProcessStructuredInputsResponse.class, name = "PROCESS"),
})
public abstract class StructuredInputsResponse<T extends Input> {

    @JsonProperty("inputs")
    private Collection<T> inputs;

    @JsonProperty("@type")
    private ModelDomain modelDomain;

    protected StructuredInputsResponse() {
    }

    protected StructuredInputsResponse(Collection<T> inputs, ModelDomain modelDomain) {
        this.inputs = inputs;
        this.modelDomain = modelDomain;
    }

    public Collection<T> getInputs() {
        return inputs;
    }
}
