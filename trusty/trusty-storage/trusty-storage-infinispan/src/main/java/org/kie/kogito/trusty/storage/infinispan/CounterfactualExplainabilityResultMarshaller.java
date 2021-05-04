/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import org.kie.kogito.trusty.storage.api.model.BaseExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.CounterfactualExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.ExplainabilityStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CounterfactualExplainabilityResultMarshaller extends AbstractModelMarshaller<CounterfactualExplainabilityResult> {

    public CounterfactualExplainabilityResultMarshaller(ObjectMapper mapper) {
        super(mapper, CounterfactualExplainabilityResult.class);
    }

    @Override
    public CounterfactualExplainabilityResult readFrom(ProtoStreamReader reader) throws IOException {
        return new CounterfactualExplainabilityResult(
                reader.readString(BaseExplainabilityResult.EXECUTION_ID_FIELD),
                reader.readString(CounterfactualExplainabilityResult.COUNTERFACTUAL_ID_FIELD),
                reader.readString(CounterfactualExplainabilityResult.COUNTERFACTUAL_SOLUTION_ID_FIELD),
                enumFromString(reader.readString(BaseExplainabilityResult.STATUS_FIELD), ExplainabilityStatus.class),
                reader.readString(BaseExplainabilityResult.STATUS_DETAILS_FIELD));
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, CounterfactualExplainabilityResult input) throws IOException {
        writer.writeString(BaseExplainabilityResult.EXECUTION_ID_FIELD, input.getExecutionId());
        writer.writeString(CounterfactualExplainabilityResult.COUNTERFACTUAL_ID_FIELD, input.getCounterfactualId());
        writer.writeString(CounterfactualExplainabilityResult.COUNTERFACTUAL_SOLUTION_ID_FIELD, input.getSolutionId());
        writer.writeString(BaseExplainabilityResult.STATUS_FIELD, stringFromEnum(input.getStatus()));
        writer.writeString(BaseExplainabilityResult.STATUS_DETAILS_FIELD, input.getStatusDetails());
    }
}
