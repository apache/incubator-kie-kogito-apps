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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.api.model.ExecutionTypeEnum;

public class DecisionMarshaller extends AbstractMarshaller implements MessageMarshaller<Decision> {

    private static final ObjectMapper myMapper = new ObjectMapper();

    public DecisionMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Decision readFrom(ProtoStreamReader reader) throws IOException {
        Decision result = new Decision();

        result.setExecutionId(reader.readString("executionId"));
        result.setExecutionTimestamp(reader.readLong("executionTimestamp"));
        result.setSuccess(reader.readBoolean("hasSucceeded"));
        result.setExecutorName(reader.readString("executorName"));
        result.setExecutedModelName(reader.readString("executedModelName"));
        result.setExecutionType(myMapper.readValue(reader.readString("executionType"), ExecutionTypeEnum.class));

        return result;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Decision result) throws IOException {
        writer.writeString("executionId", result.getExecutionId());
        writer.writeLong("executionTimestamp", result.getExecutionTimestamp());
        writer.writeBoolean("hasSucceeded", result.hasSucceeded());
        writer.writeString("executorName", result.getExecutorName());
        writer.writeString("executedModelName", result.getExecutedModelName());
        writer.writeString("executionType", myMapper.writeValueAsString(result.getExecutionType()));
    }

    @Override
    public Class<? extends Decision> getJavaClass() {
        return Decision.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }
}
