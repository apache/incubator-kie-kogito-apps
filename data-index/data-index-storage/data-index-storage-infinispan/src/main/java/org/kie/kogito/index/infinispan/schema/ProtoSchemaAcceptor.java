package org.kie.kogito.index.infinispan.schema;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.persistence.api.schema.SchemaAcceptor;
import org.kie.kogito.persistence.api.schema.SchemaType;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;
import static org.kie.kogito.persistence.infinispan.Constants.INFINISPAN_STORAGE;

@ApplicationScoped
public class ProtoSchemaAcceptor implements SchemaAcceptor {

    public static final String PROTO_SCHEMA_TYPE = "proto";

    @ConfigProperty(name = PERSISTENCE_TYPE_PROPERTY)
    public Optional<String> storageType;

    @Override
    public boolean accept(SchemaType type) {
        return storageType.isPresent() && INFINISPAN_STORAGE.equals(storageType.get()) && PROTO_SCHEMA_TYPE.equals(type.getType());
    }
}
