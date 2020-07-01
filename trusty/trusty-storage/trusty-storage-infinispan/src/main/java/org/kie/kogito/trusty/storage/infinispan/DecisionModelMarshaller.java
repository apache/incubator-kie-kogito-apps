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

package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.api.model.DecisionOutcome;
import org.kie.kogito.trusty.storage.api.model.Execution;
import org.kie.kogito.trusty.storage.api.model.ExecutionTypeEnum;
import org.kie.kogito.trusty.storage.api.model.TypedValue;

public class DecisionModelMarshaller extends AbstractModelMarshaller<Decision> {

    public DecisionModelMarshaller(ObjectMapper mapper) {
        super(mapper, Decision.class);
    }

    @Override
    public Decision readFrom(ProtoStreamReader reader) throws IOException {
        Decision result = new Decision();
        result.setExecutionId(reader.readString(Execution.EXECUTION_ID));
        result.setExecutionTimestamp(reader.readLong(Execution.EXECUTION_TIMESTAMP));
        result.setSuccess(reader.readBoolean(Execution.HAS_SUCCEEDED));
        result.setExecutorName(reader.readString(Execution.EXECUTOR_NAME));
        result.setExecutedModelName(reader.readString(Execution.EXECUTED_MODEL_NAME));
        result.setExecutionType(enumFromString(reader.readString(Execution.EXECUTION_TYPE), ExecutionTypeEnum.class));
        result.setInputs(reader.readCollection("inputs", new ArrayList<>(), TypedValue.class));
        result.setOutcomes(reader.readCollection("outcomes", new ArrayList<>(), DecisionOutcome.class));
        return result;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Decision result) throws IOException {
        writer.writeString(Execution.EXECUTION_ID, result.getExecutionId());
        writer.writeLong(Execution.EXECUTION_TIMESTAMP, result.getExecutionTimestamp());
        writer.writeBoolean(Execution.HAS_SUCCEEDED, result.hasSucceeded());
        writer.writeString(Execution.EXECUTOR_NAME, result.getExecutorName());
        writer.writeString(Execution.EXECUTED_MODEL_NAME, result.getExecutedModelName());
        writer.writeString(Execution.EXECUTION_TYPE, stringFromEnum(result.getExecutionType()));
        writer.writeCollection("inputs", result.getInputs(), TypedValue.class);
        writer.writeCollection("outcomes", result.getOutcomes(), DecisionOutcome.class);
    }
}
