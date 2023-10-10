package org.kie.kogito.explainability.api;

import java.util.Collection;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LIMEExplainabilityRequest extends BaseExplainabilityRequest {

    public static final String EXPLAINABILITY_TYPE_NAME = "LIME";

    @JsonProperty("inputs")
    @NotNull(message = "inputs object must be provided.")
    private Collection<NamedTypedValue> inputs;

    @JsonProperty("outputs")
    @NotNull(message = "outputs object must be provided.")
    private Collection<NamedTypedValue> outputs;

    private LIMEExplainabilityRequest() {
        super();
    }

    public LIMEExplainabilityRequest(@NotNull String executionId,
            @NotBlank String serviceUrl,
            @NotNull ModelIdentifier modelIdentifier,
            @NotNull Collection<NamedTypedValue> inputs,
            @NotNull Collection<NamedTypedValue> outputs) {
        super(executionId, serviceUrl, modelIdentifier);
        this.inputs = Objects.requireNonNull(inputs);
        this.outputs = Objects.requireNonNull(outputs);
    }

    public Collection<NamedTypedValue> getInputs() {
        return inputs;
    }

    public Collection<NamedTypedValue> getOutputs() {
        return outputs;
    }

}
