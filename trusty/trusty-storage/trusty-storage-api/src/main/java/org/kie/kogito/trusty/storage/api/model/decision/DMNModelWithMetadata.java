package org.kie.kogito.trusty.storage.api.model.decision;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.storage.api.model.ModelWithMetadata;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class DMNModelWithMetadata extends ModelWithMetadata<DMNModelMetadata> {

    public static final String MODEL_FIELD = "model";

    @JsonProperty(MODEL_FIELD)
    private String model;

    public DMNModelWithMetadata() {
    }

    public DMNModelWithMetadata(String groupId, String artifactId, String modelVersion, String dmnVersion,
            String name, String namespace, String model) {
        this(new DMNModelMetadata(groupId, artifactId, modelVersion, dmnVersion, name, namespace), model);
    }

    public DMNModelWithMetadata(DMNModelMetadata dmnModelMetadata, String model) {
        super(dmnModelMetadata, ModelDomain.DECISION);
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDmnVersion() {
        return modelMetaData.getDmnVersion();
    }

    public String getName() {
        return modelMetaData.getName();
    }

    public String getNamespace() {
        return modelMetaData.getNamespace();
    }
}
