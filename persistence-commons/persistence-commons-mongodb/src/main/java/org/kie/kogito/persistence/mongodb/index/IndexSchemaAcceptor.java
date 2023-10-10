package org.kie.kogito.persistence.mongodb.index;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.persistence.api.schema.SchemaAcceptor;
import org.kie.kogito.persistence.api.schema.SchemaType;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;
import static org.kie.kogito.persistence.mongodb.Constants.MONGODB_STORAGE;

@ApplicationScoped
public class IndexSchemaAcceptor implements SchemaAcceptor {

    @ConfigProperty(name = PERSISTENCE_TYPE_PROPERTY)
    Optional<String> storageType;

    @Override
    public boolean accept(SchemaType type) {
        return storageType.isPresent() && MONGODB_STORAGE.equals(storageType.get());
    }
}
