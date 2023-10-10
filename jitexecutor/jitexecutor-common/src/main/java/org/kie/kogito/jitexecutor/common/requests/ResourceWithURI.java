package org.kie.kogito.jitexecutor.common.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceWithURI {

    private String URI;
    private String content;

    public ResourceWithURI() {
    }

    public ResourceWithURI(String URI, String content) {
        this.URI = URI;
        this.content = content;
    }

    @JsonProperty("URI")
    public String getURI() {
        return URI;
    }

    public void setURI(String uRI) {
        URI = uRI;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
