package org.kie.kogito.trusty.storage.api.model;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.storage.api.model.decision.DMNModelWithMetadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "@type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DMNModelWithMetadata.class, name = "DECISION")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ModelWithMetadata<T extends ModelMetadata> {

    public static final String MODEL_METADATA = "modelMetaData";

    @JsonProperty(MODEL_METADATA)
    protected T modelMetaData;

    @JsonProperty("@type")
    private ModelDomain modelDomain;

    protected ModelWithMetadata() {
    }

    protected ModelWithMetadata(T modelMetaData, ModelDomain modelDomain) {
        this.modelMetaData = modelMetaData;
        this.modelDomain = modelDomain;
    }

    public T getModelMetaData() {
        return modelMetaData;
    }

    public void setModelMetaData(T modelMetaData) {
        this.modelMetaData = modelMetaData;
    }

    public String getGroupId() {
        return modelMetaData.getGroupId();
    }

    public String getArtifactId() {
        return modelMetaData.getArtifactId();
    }

    public String getModelVersion() {
        return modelMetaData.getModelVersion();
    }

    public String getIdentifier() {
        return modelMetaData.getIdentifier();
    }
}
