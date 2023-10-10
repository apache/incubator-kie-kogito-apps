package org.kie.kogito.persistence.mongodb.index;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.schema.SchemaType;

import static org.kie.kogito.persistence.mongodb.Constants.MONGODB_STORAGE;
import static org.wildfly.common.Assert.assertFalse;
import static org.wildfly.common.Assert.assertTrue;

class IndexSchemaAcceptorTest {

    IndexSchemaAcceptor indexSchemaAcceptor = new IndexSchemaAcceptor();

    @Test
    void supportedStorageType() {
        indexSchemaAcceptor.storageType = Optional.of(MONGODB_STORAGE);
        assertTrue(indexSchemaAcceptor.accept(new SchemaType("test")));
    }

    @Test
    void unsupportedStorageType() {
        indexSchemaAcceptor.storageType = Optional.of("test");
        assertFalse(indexSchemaAcceptor.accept(new SchemaType("test")));
    }
}