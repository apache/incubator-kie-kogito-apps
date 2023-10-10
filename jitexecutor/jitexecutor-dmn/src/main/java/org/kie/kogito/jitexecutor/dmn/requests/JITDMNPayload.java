package org.kie.kogito.jitexecutor.dmn.requests;

import java.util.List;
import java.util.Map;

import org.kie.kogito.jitexecutor.common.requests.MultipleResourcesPayload;
import org.kie.kogito.jitexecutor.common.requests.ResourceWithURI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JITDMNPayload extends MultipleResourcesPayload {

    private String model;
    private Map<String, Object> context;

    public JITDMNPayload() {
    }

    public JITDMNPayload(String model, Map<String, Object> context) {
        this.model = model;
        this.context = context;
    }

    public JITDMNPayload(String mainURI, List<ResourceWithURI> resources, Map<String, Object> context) {
        super(mainURI, resources);
        this.context = context;
    }

    @Override
    public List<ResourceWithURI> getResources() {
        consistencyChecks();
        return super.getResources();
    }

    public String getModel() {
        consistencyChecks();
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    private void consistencyChecks() {
        if (model != null && getMainURI() != null && getResources() != null && !getResources().isEmpty()) {
            throw new IllegalStateException("JITDMNPayload should not contain both (main) model and resources collection");
        }
    }
}
