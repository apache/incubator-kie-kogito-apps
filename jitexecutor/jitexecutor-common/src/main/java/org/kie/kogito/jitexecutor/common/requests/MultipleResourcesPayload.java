package org.kie.kogito.jitexecutor.common.requests;

import java.util.List;

public class MultipleResourcesPayload {

    private String mainURI;
    private List<ResourceWithURI> resources;

    public MultipleResourcesPayload() {
    }

    public MultipleResourcesPayload(String mainURI, List<ResourceWithURI> resources) {
        this.mainURI = mainURI;
        this.resources = resources;
    }

    public String getMainURI() {
        return mainURI;
    }

    public void setMainURI(String mainURI) {
        this.mainURI = mainURI;
    }

    public List<ResourceWithURI> getResources() {
        return resources;
    }

    public void setResources(List<ResourceWithURI> resources) {
        this.resources = resources;
    }
}
