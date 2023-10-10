package org.kie.kogito.jitexecutor.dmn.responses;

import org.kie.kogito.trusty.service.common.responses.SalienciesResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMNResultWithExplanation {

    @JsonProperty("dmnResult")
    public JITDMNResult dmnResult;

    @JsonProperty("saliencies")
    public SalienciesResponse salienciesResponse;

    public DMNResultWithExplanation() {
    }

    public DMNResultWithExplanation(JITDMNResult dmnResult, SalienciesResponse salienciesResponse) {
        this.dmnResult = dmnResult;
        this.salienciesResponse = salienciesResponse;
    }
}
