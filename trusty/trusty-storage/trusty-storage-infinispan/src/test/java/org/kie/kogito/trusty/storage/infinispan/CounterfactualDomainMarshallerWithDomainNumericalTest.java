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
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.trusty.storage.api.model.CounterfactualDomain;
import org.kie.kogito.trusty.storage.api.model.CounterfactualDomainNumerical;
import org.kie.kogito.trusty.storage.infinispan.testfield.AbstractTestField;
import org.kie.kogito.trusty.storage.infinispan.testfield.EnumTestField;
import org.kie.kogito.trusty.storage.infinispan.testfield.ObjectTestField;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.kie.kogito.trusty.storage.api.model.CounterfactualDomain.NUMERICAL;
import static org.kie.kogito.trusty.storage.api.model.CounterfactualDomain.TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CounterfactualDomainMarshallerWithDomainNumericalTest extends MarshallerTestTemplate<CounterfactualDomain> {

    private static final CounterfactualDomainNumerical domain = new CounterfactualDomainNumerical();

    private static final Function<CounterfactualDomain, CounterfactualDomainNumerical> getter = mock(Function.class);

    private static final BiConsumer<CounterfactualDomain, CounterfactualDomainNumerical> setter = mock(BiConsumer.class);

    private static final List<AbstractTestField<CounterfactualDomain, ?>> TEST_FIELD_LIST = List.of(
            new EnumTestField<>(TYPE,
                    CounterfactualDomain.Type.NUMERICAL,
                    CounterfactualDomain::getType,
                    CounterfactualDomain::setType,
                    CounterfactualDomain.Type.class),
            new ObjectTestField<>(NUMERICAL,
                    domain,
                    getter,
                    setter,
                    CounterfactualDomainNumerical.class));

    public CounterfactualDomainMarshallerWithDomainNumericalTest() {
        super(CounterfactualDomain.class);
        when(getter.apply(any())).thenReturn(domain);
    }

    @Override
    protected CounterfactualDomainNumerical buildEmptyObject() {
        return domain;
    }

    @Override
    protected MessageMarshaller<CounterfactualDomain> buildMarshaller() {
        return new CounterfactualDomainMarshaller(new ObjectMapper()) {
            @Override
            public CounterfactualDomain readFrom(ProtoStreamReader reader) throws IOException {
                when(reader.readObject(eq(NUMERICAL), any())).thenReturn(domain);
                return super.readFrom(reader);
            }
        };
    }

    @Override
    protected List<AbstractTestField<CounterfactualDomain, ?>> getTestFieldList() {
        return TEST_FIELD_LIST;
    }

}
