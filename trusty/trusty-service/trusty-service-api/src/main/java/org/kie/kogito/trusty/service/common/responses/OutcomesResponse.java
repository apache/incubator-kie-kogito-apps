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

package org.kie.kogito.trusty.service.common.responses;

import java.util.Collection;

import org.kie.kogito.trusty.storage.api.model.Outcome;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base abstract class for <b>OutcomesResponse</b>
 * 
 * @param <T>
 * @param <E>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class OutcomesResponse<T extends ExecutionHeaderResponse, E extends Outcome> {

    @JsonProperty("header")
    private T header;

    @JsonProperty("outcomes")
    private Collection<E> outcomes;

    private OutcomesResponse() {
    }

    public OutcomesResponse(T header, Collection<E> outcomes) {
        this.header = header;
        this.outcomes = outcomes;
    }

    public T getHeader() {
        return header;
    }

    public Collection<E> getOutcomes() {
        return outcomes;
    }
}
