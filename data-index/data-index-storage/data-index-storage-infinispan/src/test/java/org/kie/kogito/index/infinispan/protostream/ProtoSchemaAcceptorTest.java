package org.kie.kogito.index.infinispan.protostream;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.kie.kogito.index.infinispan.schema.ProtoSchemaAcceptor;
import org.kie.kogito.persistence.api.schema.SchemaType;

import static org.kie.kogito.persistence.infinispan.Constants.INFINISPAN_STORAGE;
import static org.wildfly.common.Assert.assertFalse;
import static org.wildfly.common.Assert.assertTrue;

class ProtoSchemaAcceptorTest {

    ProtoSchemaAcceptor protoSchemaAcceptor = new ProtoSchemaAcceptor();

    @Test
    void supportedStorageTypeAndSchemaType() {
        protoSchemaAcceptor.storageType = Optional.of(INFINISPAN_STORAGE);
        assertTrue(protoSchemaAcceptor.accept(new SchemaType(ProtoSchemaAcceptor.PROTO_SCHEMA_TYPE)));
    }

    @Test
    void unsupportedSchemaType() {
        protoSchemaAcceptor.storageType = Optional.of(INFINISPAN_STORAGE);
        assertFalse(protoSchemaAcceptor.accept(new SchemaType("test")));
    }

    @Test
    void unsupportedStorageType() {
        protoSchemaAcceptor.storageType = Optional.of("test");
        assertFalse(protoSchemaAcceptor.accept(new SchemaType(ProtoSchemaAcceptor.PROTO_SCHEMA_TYPE)));
    }
}