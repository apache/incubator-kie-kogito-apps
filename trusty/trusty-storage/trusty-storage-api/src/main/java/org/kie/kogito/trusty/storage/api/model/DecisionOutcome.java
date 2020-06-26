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

import java.util.List;

public class DecisionOutcome {

    private String outcomeId;
    private String outcomeName;
    private String evaluationStatus;
    private TypedValue outcomeResult;
    private List<TypedValue> outcomeInputs;
    private List<Message> messages;
    private boolean hasErrors;

    public DecisionOutcome(String outcomeId, String outcomeName, String evaluationStatus, TypedValue outcomeResult, List<TypedValue> outcomeInputs, List<Message> messages) {
        this.outcomeId = outcomeId;
        this.outcomeName = outcomeName;
        this.evaluationStatus = evaluationStatus;
        this.outcomeResult = outcomeResult;
        this.outcomeInputs = outcomeInputs;
        setMessages(messages);
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

    public List<TypedValue> getOutcomeInputs() {
        return outcomeInputs;
    }

    public void setOutcomeInputs(List<TypedValue> outcomeInputs) {
        this.outcomeInputs = outcomeInputs;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        this.hasErrors = messages != null && messages.stream().anyMatch(m -> m.getLevel() == Message.Level.ERROR);
    }

    public boolean hasErrors() {
        return hasErrors;
    }

}
