package org.kie.kogito.persistence.protobuf;

import javax.enterprise.event.Event;

import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.config.Configuration;
import org.infinispan.protostream.descriptors.FileDescriptor;
import org.infinispan.protostream.impl.SerializationContextImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.persistence.api.proto.DomainModelRegisteredEvent;
import org.kie.kogito.persistence.protobuf.domain.ProtoDomainModelProducer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.kie.kogito.persistence.protobuf.ProtobufService.DOMAIN_MODEL_PROTO_NAME;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProtoDomainModelProducerTest {

    @Mock
    Event<DomainModelRegisteredEvent> domainEvent;

    @InjectMocks
    ProtoDomainModelProducer protoDomainModelProducer;

    @Test
    void onFileDescriptorRegistered() {
        FileDescriptor fileDescriptor = getTestFileDescriptor();
        FileDescriptorRegisteredEvent event = new FileDescriptorRegisteredEvent(fileDescriptor);
        protoDomainModelProducer.onFileDescriptorRegistered(event);

        verify(domainEvent).fire(eq(new DomainModelRegisteredEvent(TestUtils.PROCESS_ID, TestUtils.DOMAIN_DESCRIPTOR, TestUtils.ADDITIONAL_DESCRIPTORS)));
    }

    static FileDescriptor getTestFileDescriptor() {
        String content = TestUtils.getTestFileContent();
        SerializationContext ctx = new SerializationContextImpl(Configuration.builder().build());
        ctx.registerProtoFiles(FileDescriptorSource.fromString(DOMAIN_MODEL_PROTO_NAME, content));
        return ctx.getFileDescriptors().get(DOMAIN_MODEL_PROTO_NAME);
    }
}