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

import org.kie.kogito.trusty.storage.api.model.CounterfactualDomainNumerical;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CounterfactualDomainNumericalMarshaller extends AbstractModelMarshaller<CounterfactualDomainNumerical> {

    public CounterfactualDomainNumericalMarshaller(ObjectMapper mapper) {
        super(mapper, CounterfactualDomainNumerical.class);
    }

    @Override
    public CounterfactualDomainNumerical readFrom(ProtoStreamReader reader) throws IOException {
        return new CounterfactualDomainNumerical(
                reader.readDouble(CounterfactualDomainNumerical.LOWER_BOUND),
                reader.readDouble(CounterfactualDomainNumerical.UPPER_BOUND));
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, CounterfactualDomainNumerical input) throws IOException {
        writer.writeDouble(CounterfactualDomainNumerical.LOWER_BOUND, input.getLowerBound());
        writer.writeDouble(CounterfactualDomainNumerical.UPPER_BOUND, input.getUpperBound());
    }
}
