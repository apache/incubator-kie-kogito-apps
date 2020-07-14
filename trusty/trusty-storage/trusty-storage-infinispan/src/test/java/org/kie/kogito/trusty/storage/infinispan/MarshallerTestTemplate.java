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
import java.util.List;

import org.infinispan.protostream.MessageMarshaller;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.storage.infinispan.testfield.AbstractTestField;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

abstract class MarshallerTestTemplate<T> {

    protected abstract T buildEmptyObject();

    protected abstract MessageMarshaller<T> buildMarshaller();

    protected abstract List<AbstractTestField<T, ?>> getTestFieldList();

    @Test
    void allPropertiesAreCoveredByTheMarshaller() throws IOException {
        List<AbstractTestField<T, ?>> list = getTestFieldList();

        T object = buildEmptyObject();
        list.forEach(td -> td.setValue(object));

        MessageMarshaller<T> marshaller = buildMarshaller();
        ProtoStreamWriterMock protoStreamWriter = new ProtoStreamWriterMock();
        marshaller.writeTo(protoStreamWriter, object);

        List<String> usedFields = protoStreamWriter.getWrittenFieldNames();

        assertEquals(list.size(), usedFields.size());
        list.forEach(td -> assertTrue(usedFields.contains(td.getFieldName())));
    }

    @Test
    void allPropertiesAreCoveredByTheUnmarshaller() throws IOException {
        List<AbstractTestField<T, ?>> list = getTestFieldList();

        MessageMarshaller.ProtoStreamReader protoStreamReader = mock(MessageMarshaller.ProtoStreamReader.class);
        for(AbstractTestField<T, ?> td : list) {
            td.mockReader(protoStreamReader);
        }

        MessageMarshaller<T> marshaller = buildMarshaller();
        T output = marshaller.readFrom(protoStreamReader);

        assertEquals(list.size(), mockingDetails(protoStreamReader).getInvocations().size());
        list.forEach(td -> td.assertValue(output));
    }
}
