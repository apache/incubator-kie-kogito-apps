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