package org.kie.kogito.trusty.storage.api.model.decision;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.storage.api.model.ModelMetadata;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class DMNModelMetadata extends ModelMetadata {

    public static final String DMN_VERSION_FIELD = "dmnVersion";
    public static final String NAME_FIELD = "name";
    public static final String NAMESPACE_FIELD = "namespace";

    @JsonProperty(DMN_VERSION_FIELD)
    private String dmnVersion;

    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonProperty(NAMESPACE_FIELD)
    private String namespace;

    private String identifier = null;

    public DMNModelMetadata() {
    }

    public DMNModelMetadata(String groupId, String artifactId, String modelVersion, String dmnVersion,
            String name, String namespace) {
        super(groupId, artifactId, modelVersion, ModelDomain.DECISION);
        this.dmnVersion = dmnVersion;
        this.name = name;
        this.namespace = namespace;
        this.identifier = makeIdentifier(name, namespace);
    }

    public String getDmnVersion() {
        return dmnVersion;
    }

    public void setDmnVersion(String dmnVersion) {
        this.dmnVersion = dmnVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }
}
