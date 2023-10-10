package org.kie.kogito.persistence.protobuf;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.infinispan.protostream.DescriptorParserException;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.descriptors.FieldDescriptor;
import org.infinispan.protostream.descriptors.FileDescriptor;
import org.infinispan.protostream.impl.SerializationContextImpl;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.schema.AttributeDescriptor;
import org.kie.kogito.persistence.api.schema.EntityIndexDescriptor;

import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.kie.kogito.persistence.protobuf.ProtoIndexParser.INDEXED_ANNOTATION;
import static org.kie.kogito.persistence.protobuf.ProtoIndexParser.configureBuilder;
import static org.kie.kogito.persistence.protobuf.ProtoIndexParser.createAttributeDescriptor;
import static org.kie.kogito.persistence.protobuf.ProtoIndexParser.createEntityIndexDescriptors;
import static org.kie.kogito.persistence.protobuf.TestUtils.getTestFileContent;
import static org.kie.kogito.persistence.protobuf.TestUtils.getValidEntityIndexDescriptors;

class ProtoIndexParserTest {

    @Test
    void testConfigureBuilder() {
        Map<String, EntityIndexDescriptor> entityIndexes = createFileDescriptor().getMessageTypes().stream().map(t -> t.<EntityIndexDescriptor> getProcessedAnnotation(INDEXED_ANNOTATION))
                .filter(Objects::nonNull).collect(toMap(EntityIndexDescriptor::getName, Function.identity()));
        assertEquals(getValidEntityIndexDescriptors(false), entityIndexes);
    }

    @Test
    void testConfigureBuilderWithInvalidFile() {
        SerializationContext ctx = new SerializationContextImpl(configureBuilder().build());
        FileDescriptorSource invalidFileDescriptorSource = FileDescriptorSource.fromString("invalid", "invalid");
        try {
            ctx.registerProtoFiles(invalidFileDescriptorSource);
            fail("Failed to process invalid proto file");
        } catch (DescriptorParserException ex) {
            // Successfully throw exception
        }
    }

    @Test
    void testCreateEntityIndexeDescriptors() {
        FileDescriptor fileDescriptor = createFileDescriptor();
        Map<String, EntityIndexDescriptor> entityIndexes = createFileDescriptor().getMessageTypes().stream().map(t -> t.<EntityIndexDescriptor> getProcessedAnnotation(INDEXED_ANNOTATION))
                .filter(Objects::nonNull).collect(toMap(EntityIndexDescriptor::getName, Function.identity()));

        Map<String, EntityIndexDescriptor> indexDescriptor = createEntityIndexDescriptors(fileDescriptor, entityIndexes);

        assertEquals(getValidEntityIndexDescriptors(true), indexDescriptor);
    }

    @Test
    void testCreateAttributeDescriptor() {
        FieldDescriptor roomField = createFileDescriptor().getMessageTypes().stream()
                .filter(descriptor -> "org.acme.travels.travels.Hotel".equals(descriptor.getFullName())).findAny().get().findFieldByName("room");

        AttributeDescriptor attributeDescriptor = createAttributeDescriptor(roomField, null);
        assertEquals(new AttributeDescriptor("room", "string", true), attributeDescriptor);
    }

    private FileDescriptor createFileDescriptor() {
        SerializationContext ctx = new SerializationContextImpl(configureBuilder().build());
        String content = getTestFileContent();
        FileDescriptorSource fileDescriptorSource = FileDescriptorSource.fromString("test", content);
        ctx.registerProtoFiles(fileDescriptorSource);
        return ctx.getFileDescriptors().get("test");
    }
}