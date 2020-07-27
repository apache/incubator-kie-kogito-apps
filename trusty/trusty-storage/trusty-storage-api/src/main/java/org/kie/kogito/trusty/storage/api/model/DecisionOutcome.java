/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kie.kogito.trusty.storage.api.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.kie.kogito.tracing.decision.event.common.MessageLevel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DecisionOutcome {

    public static final String EVALUATION_STATUS_FIELD = "evaluationStatus";
    public static final String MESSAGES_FIELD = "messages";
    public static final String OUTCOME_ID_FIELD = "outcomeId";
    public static final String OUTCOME_INPUTS_FIELD = "outcomeInputs";
    public static final String OUTCOME_NAME_FIELD = "outcomeName";
    public static final String OUTCOME_RESULT_FIELD = "outcomeResult";

    @JsonProperty(OUTCOME_ID_FIELD)
    private String outcomeId;

    @JsonProperty(OUTCOME_NAME_FIELD)
    private String outcomeName;

    @JsonProperty(EVALUATION_STATUS_FIELD)
    private String evaluationStatus;

    @JsonProperty(OUTCOME_RESULT_FIELD)
    private TypedValue outcomeResult;

    @JsonProperty(OUTCOME_INPUTS_FIELD)
    private Collection<TypedValue> outcomeInputs;

    @JsonProperty(MESSAGES_FIELD)
    private Collection<Message> messages;

    public DecisionOutcome() {
    }

    public DecisionOutcome(String outcomeId, String outcomeName, String evaluationStatus, TypedValue outcomeResult, Collection<TypedValue> outcomeInputs, Collection<Message> messages) {
        this.outcomeId = outcomeId;
        this.outcomeName = outcomeName;
        this.evaluationStatus = evaluationStatus;
        this.outcomeResult = outcomeResult;
        this.outcomeInputs = outcomeInputs;
        this.messages = messages;
    }

    public String getOutcomeId() {
        return outcomeId;
    }

    public void setOutcomeId(String outcomeId) {
        this.outcomeId = outcomeId;
    }

    public String getOutcomeName() {
        return outcomeName;
    }

    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }

    public String getEvaluationStatus() {
        return evaluationStatus;
    }

    public void setEvaluationStatus(String evaluationStatus) {
        this.evaluationStatus = evaluationStatus;
    }

    public TypedValue getOutcomeResult() {
        return outcomeResult;
    }

    public void setOutcomeResult(TypedValue outcomeResult) {
        this.outcomeResult = outcomeResult;
    }

    public Collection<TypedValue> getOutcomeInputs() {
        return outcomeInputs;
    }

    public void setOutcomeInputs(Collection<TypedValue> outcomeInputs) {
        this.outcomeInputs = outcomeInputs;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

    public boolean hasErrors() {
        return messages != null && messages.stream().anyMatch(m -> m.getLevel() == MessageLevel.ERROR);
    }
}
