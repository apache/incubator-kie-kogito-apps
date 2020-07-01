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

package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kie.kogito.trusty.storage.api.model.DecisionOutcome;
import org.kie.kogito.trusty.storage.api.model.Message;
import org.kie.kogito.trusty.storage.api.model.TypedValue;

/*
private String outcomeId;
    private String outcomeName;
    private String evaluationStatus;
    private TypedValue outcomeResult;
    private List<TypedValue> outcomeInputs;
    private List<Message> messages;
    private boolean hasErrors;
*/
public class DecisionOutcomeModelMarshaller extends AbstractModelMarshaller<DecisionOutcome> {

    public DecisionOutcomeModelMarshaller(ObjectMapper mapper) {
        super(mapper, DecisionOutcome.class);
    }

    @Override
    public DecisionOutcome readFrom(ProtoStreamReader reader) throws IOException {
        return new DecisionOutcome(
                reader.readString("outcomeId"),
                reader.readString("outcomeName"),
                reader.readString("evaluationStatus"),
                reader.readObject("outcomeResult", TypedValue.class),
                reader.readCollection("outcomeInputs", new ArrayList<>(), TypedValue.class),
                reader.readCollection("messages", new ArrayList<>(), Message.class)
        );
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, DecisionOutcome input) throws IOException {
        writer.writeString("outcomeId", input.getOutcomeId());
        writer.writeString("outcomeName", input.getOutcomeName());
        writer.writeString("evaluationStatus", input.getEvaluationStatus());
        writer.writeObject("outcomeResult", input.getOutcomeResult(), TypedValue.class);
        writer.writeCollection("outcomeInputs", input.getOutcomeInputs(), TypedValue.class);
        writer.writeCollection("messages", input.getMessages(), Message.class);
    }
}
