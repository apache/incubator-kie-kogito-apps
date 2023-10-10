package org.kie.kogito.explainability.api;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelIdentifier {

    public static final String RESOURCE_ID_SEPARATOR = ":";

    @JsonProperty("resourceType")
    @NotBlank(message = "resourceType must be not blank.")
    private String resourceType;

    @JsonProperty("resourceId")
    @NotBlank(message = "resourceId must be not blank.")
    private String resourceId;

    public ModelIdentifier() {
    }

    public ModelIdentifier(@NotBlank String resourceType,
            @NotBlank String resourceId) {
        this.resourceType = Objects.requireNonNull(resourceType);
        this.resourceId = Objects.requireNonNull(resourceId);
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}
