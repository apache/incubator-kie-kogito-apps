package org.kie.kogito.persistence.mongodb.mock;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;
import org.kie.kogito.persistence.mongodb.storage.MongoModelService;

import io.quarkus.test.Mock;

@Mock
@ApplicationScoped
public class MockMongoModelService implements MongoModelService {

    @SuppressWarnings("unchecked")
    @Override
    public <V, E> MongoEntityMapper<V, E> getEntityMapper(String name) {
        return (MongoEntityMapper<V, E>) new MockMongoEntityMapper();
    }
}
