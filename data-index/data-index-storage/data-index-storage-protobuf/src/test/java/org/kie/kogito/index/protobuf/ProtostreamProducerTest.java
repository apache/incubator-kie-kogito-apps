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

package org.kie.kogito.index.protobuf;

import java.io.IOException;

import org.infinispan.protostream.FileDescriptorSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.kie.kogito.index.protobuf.ProtostreamProducer.KOGITO_INDEX_PROTO;
import static org.kie.kogito.index.protobuf.ProtostreamProducer.KOGITO_TYPES_PROTO;

@ExtendWith(MockitoExtension.class)
class ProtostreamProducerTest {

    @InjectMocks
    ProtostreamProducer protostreamProducer;

    @Test
    void kogitoTypesDescriptor() {
        try {
            FileDescriptorSource fileDescriptorSource = protostreamProducer.kogitoTypesDescriptor();

            assertTrue(fileDescriptorSource.getFileDescriptors().containsKey(KOGITO_INDEX_PROTO));
            assertTrue(fileDescriptorSource.getFileDescriptors().containsKey(KOGITO_TYPES_PROTO));
        } catch (IOException e) {
            fail("Failed with IOException", e);
        }
    }
}