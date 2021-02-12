/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.explainability.api;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExplainabilityResultDto {

    @JsonProperty("executionId")
    private String executionId;

    @JsonProperty("status")
    private ExplainabilityStatus status;

    @JsonProperty("statusDetails")
    private String statusDetails;

    @JsonProperty("saliency")
    private Map<String, SaliencyDto> saliencies;

    private ExplainabilityResultDto() {
    }

    private ExplainabilityResultDto(String executionId, ExplainabilityStatus status, String statusDetails, Map<String, SaliencyDto> saliencies) {
        this.executionId = executionId;
        this.status = status;
        this.statusDetails = statusDetails;
        this.saliencies = saliencies;
    }

    public String getExecutionId() {
        return executionId;
    }

    public ExplainabilityStatus getStatus() {
        return status;
    }

    public String getStatusDetails() {
        return statusDetails;
    }

    public Map<String, SaliencyDto> getSaliencies() {
        return saliencies;
    }

    public static ExplainabilityResultDto buildSucceeded(String executionId, Map<String, SaliencyDto> saliencies) {
        return new ExplainabilityResultDto(executionId, ExplainabilityStatus.SUCCEEDED, null, saliencies);
    }

    public static ExplainabilityResultDto buildFailed(String executionId, String statusDetails) {
        return new ExplainabilityResultDto(executionId, ExplainabilityStatus.FAILED, statusDetails, null);
    }
}
