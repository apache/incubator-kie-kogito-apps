package org.kie.kogito.trusty.service.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SalienciesResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("saliencies")
    private List<SaliencyResponse> saliencies;

    private SalienciesResponse() {
    }

    public SalienciesResponse(String status, List<SaliencyResponse> saliencies) {
        this.status = status;
        this.saliencies = saliencies;
    }

    public String getStatus() {
        return status;
    }

    public List<SaliencyResponse> getSaliencies() {
        return saliencies;
    }
}
