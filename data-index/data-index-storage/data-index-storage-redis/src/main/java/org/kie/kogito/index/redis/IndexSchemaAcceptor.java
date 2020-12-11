package org.kie.kogito.index.redis;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.persistence.api.schema.SchemaAcceptor;
import org.kie.kogito.persistence.api.schema.SchemaType;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;
import static org.kie.kogito.persistence.redis.Constants.REDIS_STORAGE;

@ApplicationScoped
public class IndexSchemaAcceptor implements SchemaAcceptor {

    @ConfigProperty(name = PERSISTENCE_TYPE_PROPERTY)
    String storageType;

    @Override
    public boolean accept(SchemaType type) {
        return REDIS_STORAGE.equals(storageType);
    }
}