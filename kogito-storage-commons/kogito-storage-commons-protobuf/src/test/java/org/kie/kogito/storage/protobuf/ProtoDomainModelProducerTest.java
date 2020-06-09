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

package org.kie.kogito.storage.protobuf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.enterprise.event.Event;

import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.config.Configuration;
import org.infinispan.protostream.descriptors.FileDescriptor;
import org.infinispan.protostream.impl.SerializationContextImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.storage.api.proto.DomainModelRegisteredEvent;
import org.kie.kogito.storage.protobuf.domain.ProtoDomainModelProducer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.kie.kogito.storage.protobuf.ProtobufService.DOMAIN_MODEL_PROTO_NAME;
import static org.kie.kogito.storage.protobuf.TestUtils.ADDITIONAL_DESCRIPTORS;
import static org.kie.kogito.storage.protobuf.TestUtils.DOMAIN_DESCRIPTOR;
import static org.kie.kogito.storage.protobuf.TestUtils.PROCESS_ID;
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

        verify(domainEvent).fire(eq(new DomainModelRegisteredEvent(PROCESS_ID, DOMAIN_DESCRIPTOR, ADDITIONAL_DESCRIPTORS)));
    }

    static FileDescriptor getTestFileDescriptor() {
        String content = getTestFileContent();
        SerializationContext ctx = new SerializationContextImpl(Configuration.builder().build());
        ctx.registerProtoFiles(FileDescriptorSource.fromString(DOMAIN_MODEL_PROTO_NAME, content));
        return ctx.getFileDescriptors().get(DOMAIN_MODEL_PROTO_NAME);
    }

    static String getTestFileContent() {
        return getTestFileContent("test.proto");
    }

    static String getTestFileInvalidContent() {
        return getTestFileContent("test_invalid.proto");
    }

    static String getTestFileContent(String protofile) {
        try {
            Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource(protofile).toURI());
            return new String(Files.readAllBytes(path));
        } catch (URISyntaxException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}