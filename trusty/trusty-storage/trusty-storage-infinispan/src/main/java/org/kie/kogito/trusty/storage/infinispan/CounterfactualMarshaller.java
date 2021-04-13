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
import java.util.ArrayList;

import org.kie.kogito.trusty.storage.api.model.CounterfactualRequest;
import org.kie.kogito.trusty.storage.api.model.CounterfactualSearchDomain;
import org.kie.kogito.trusty.storage.api.model.TypedVariableWithValue;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CounterfactualMarshaller extends AbstractModelMarshaller<CounterfactualRequest> {

    public CounterfactualMarshaller(ObjectMapper mapper) {
        super(mapper, CounterfactualRequest.class);
    }

    @Override
    public CounterfactualRequest readFrom(ProtoStreamReader reader) throws IOException {
        return new CounterfactualRequest(
                reader.readString(CounterfactualRequest.EXECUTION_ID_FIELD),
                reader.readString(CounterfactualRequest.COUNTERFACTUAL_ID_FIELD),
                reader.readCollection(CounterfactualRequest.COUNTERFACTUAL_GOALS, new ArrayList<>(), TypedVariableWithValue.class),
                reader.readCollection(CounterfactualRequest.COUNTERFACTUAL_SEARCH_DOMAINS, new ArrayList<>(), CounterfactualSearchDomain.class));
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, CounterfactualRequest input) throws IOException {
        writer.writeString(CounterfactualRequest.EXECUTION_ID_FIELD, input.getExecutionId());
        writer.writeString(CounterfactualRequest.COUNTERFACTUAL_ID_FIELD, input.getCounterfactualId());
        writer.writeCollection(CounterfactualRequest.COUNTERFACTUAL_GOALS, input.getGoals(), TypedVariableWithValue.class);
        writer.writeCollection(CounterfactualRequest.COUNTERFACTUAL_SEARCH_DOMAINS, input.getSearchDomains(), CounterfactualSearchDomain.class);
    }
}
