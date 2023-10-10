package org.kie.kogito.explainability.api;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LIMEExplainabilityResult extends BaseExplainabilityResult {

    public static final String EXPLAINABILITY_TYPE_NAME = "lime";

    public static final String SALIENCIES_FIELD = "saliencies";

    @JsonProperty(SALIENCIES_FIELD)
    @NotNull(message = "saliencies object must be provided.")
    private List<SaliencyModel> saliencies;

    public LIMEExplainabilityResult() {
    }

    public LIMEExplainabilityResult(@NotNull String executionId,
            @NotNull ExplainabilityStatus status,
            String statusDetails,
            @NotNull List<SaliencyModel> saliencies) {
        super(executionId, status, statusDetails);
        this.saliencies = Objects.requireNonNull(saliencies);
    }

    public static LIMEExplainabilityResult buildSucceeded(String executionId, List<SaliencyModel> saliencies) {
        return new LIMEExplainabilityResult(executionId, ExplainabilityStatus.SUCCEEDED, null, saliencies);
    }

    public static LIMEExplainabilityResult buildFailed(String executionId, String statusDetails) {
        return new LIMEExplainabilityResult(executionId, ExplainabilityStatus.FAILED, statusDetails, Collections.emptyList());
    }

    public List<SaliencyModel> getSaliencies() {
        return saliencies;
    }

}
