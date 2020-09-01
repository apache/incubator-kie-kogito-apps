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
import org.kie.kogito.trusty.storage.api.model.ExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.ExplainabilityResultStatus;
import org.kie.kogito.trusty.storage.api.model.Saliency;

public class ExplainabilityResultMarshaller extends AbstractModelMarshaller<ExplainabilityResult> {

    public ExplainabilityResultMarshaller(ObjectMapper mapper) {
        super(mapper, ExplainabilityResult.class);
    }

    @Override
    public ExplainabilityResult readFrom(ProtoStreamReader reader) throws IOException {
        return new ExplainabilityResult(
                reader.readString(ExplainabilityResult.EXECUTION_ID_FIELD),
                enumFromString(reader.readString(ExplainabilityResult.STATUS_FIELD), ExplainabilityResultStatus.class),
                reader.readCollection(ExplainabilityResult.SALIENCIES_FIELD, new ArrayList<>(), Saliency.class)
        );
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, ExplainabilityResult input) throws IOException {
        writer.writeString(ExplainabilityResult.EXECUTION_ID_FIELD, input.getExecutionId());
        writer.writeString(ExplainabilityResult.STATUS_FIELD, stringFromEnum(input.getStatus()));
        writer.writeCollection(ExplainabilityResult.SALIENCIES_FIELD, input.getSaliencies(), Saliency.class);
    }

}
