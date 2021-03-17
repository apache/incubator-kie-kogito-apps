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

import java.util.Collections;
import java.util.List;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.trusty.storage.api.model.Counterfactual;
import org.kie.kogito.trusty.storage.api.model.CounterfactualSearchDomain;
import org.kie.kogito.trusty.storage.api.model.TypedVariableWithValue;
import org.kie.kogito.trusty.storage.infinispan.testfield.AbstractTestField;
import org.kie.kogito.trusty.storage.infinispan.testfield.CollectionTestField;
import org.kie.kogito.trusty.storage.infinispan.testfield.StringTestField;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.kie.kogito.trusty.storage.api.model.Counterfactual.COUNTERFACTUAL_GOALS;
import static org.kie.kogito.trusty.storage.api.model.Counterfactual.COUNTERFACTUAL_ID_FIELD;
import static org.kie.kogito.trusty.storage.api.model.Counterfactual.COUNTERFACTUAL_SEARCH_DOMAINS;
import static org.kie.kogito.trusty.storage.api.model.Counterfactual.EXECUTION_ID_FIELD;

public class CounterfactualMarshallerTest extends MarshallerTestTemplate<Counterfactual> {

    private static final List<AbstractTestField<Counterfactual, ?>> TEST_FIELD_LIST = List.of(
            new StringTestField<>(EXECUTION_ID_FIELD, "executionId", Counterfactual::getExecutionId, Counterfactual::setExecutionId),
            new StringTestField<>(COUNTERFACTUAL_ID_FIELD, "test", Counterfactual::getCounterfactualId, Counterfactual::setCounterfactualId),
            new CollectionTestField<>(COUNTERFACTUAL_GOALS, Collections.emptyList(), Counterfactual::getGoals, Counterfactual::setGoals, TypedVariableWithValue.class),
            new CollectionTestField<>(COUNTERFACTUAL_SEARCH_DOMAINS, Collections.emptyList(), Counterfactual::getSearchDomains, Counterfactual::setSearchDomains, CounterfactualSearchDomain.class));

    public CounterfactualMarshallerTest() {
        super(Counterfactual.class);
    }

    @Override
    protected Counterfactual buildEmptyObject() {
        return new Counterfactual();
    }

    @Override
    protected MessageMarshaller<Counterfactual> buildMarshaller() {
        return new CounterfactualMarshaller(new ObjectMapper());
    }

    @Override
    protected List<AbstractTestField<Counterfactual, ?>> getTestFieldList() {
        return TEST_FIELD_LIST;
    }
}
