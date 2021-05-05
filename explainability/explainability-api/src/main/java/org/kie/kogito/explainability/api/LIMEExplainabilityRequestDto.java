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

import org.kie.kogito.tracing.typedvalue.TypedValue;

public class LIMEExplainabilityRequestDto extends BaseExplainabilityRequestDto {

    public static final String EXPLAINABILITY_TYPE_NAME = "LIME";

    private LIMEExplainabilityRequestDto() {
        super();
    }

    public LIMEExplainabilityRequestDto(String executionId, String serviceUrl, ModelIdentifierDto modelIdentifier, Map<String, TypedValue> inputs, Map<String, TypedValue> outputs) {
        super(executionId, serviceUrl, modelIdentifier, inputs, outputs);
    }
}
