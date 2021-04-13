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

package org.kie.kogito.explainability.models;

import java.util.Map;

import org.kie.kogito.explainability.api.BaseExplainabilityRequestDto;
import org.kie.kogito.explainability.api.LIMEExplainabilityRequestDto;
import org.kie.kogito.tracing.typedvalue.TypedValue;

public class ExplainabilityRequest {

    private final String executionId;
    private final String serviceUrl;
    private final ModelIdentifier modelIdentifier;
    private final Map<String, TypedValue> inputs;
    private final Map<String, TypedValue> outputs;

    private ExplainabilityRequest(String executionId, String serviceUrl, ModelIdentifier modelIdentifier, Map<String, TypedValue> inputs, Map<String, TypedValue> outputs) {
        this.executionId = executionId;
        this.serviceUrl = serviceUrl;
        this.modelIdentifier = modelIdentifier;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public String getExecutionId() {
        return this.executionId;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public ModelIdentifier getModelIdentifier() {
        return modelIdentifier;
    }

    public Map<String, TypedValue> getInputs() {
        return inputs;
    }

    public Map<String, TypedValue> getOutputs() {
        return outputs;
    }

    public static ExplainabilityRequest from(BaseExplainabilityRequestDto dto) {
        if (dto instanceof LIMEExplainabilityRequestDto) {
            return new ExplainabilityRequest(
                    dto.getExecutionId(),
                    dto.getServiceUrl(),
                    ModelIdentifier.from(dto.getModelIdentifier()),
                    dto.getInputs(),
                    dto.getOutputs());
        }
        //TODO ExplanationServiceImpl only supports a LIME LocalExplainer so we need to fail fast for other types.
        throw new IllegalArgumentException(String.format("Explainability result for '%s' is not supported", dto.getClass().getName()));
    }
}
