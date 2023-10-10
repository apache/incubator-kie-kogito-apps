package org.kie.kogito.persistence.mongodb.storage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;

import io.quarkus.arc.properties.IfBuildProperty;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;
import static org.kie.kogito.persistence.mongodb.Constants.MONGODB_STORAGE;

@ApplicationScoped
@IfBuildProperty(name = PERSISTENCE_TYPE_PROPERTY, stringValue = MONGODB_STORAGE)
public class MongoStorageManager implements StorageService {

    @Inject
    MongoClientManager mongoClientManager;

    @Inject
    MongoModelService mongoModelService;

    @Override
    public Storage<String, String> getCache(String name) {
        return new MongoStorage<>(
                mongoClientManager.getCollection(name, mongoModelService.getEntityMapper(name).getEntityClass()),
                String.class.getName(), mongoModelService.getEntityMapper(name));
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type) {
        return new MongoStorage<>(
                mongoClientManager.getCollection(name, mongoModelService.getEntityMapper(name).getEntityClass()),
                type.getName(), mongoModelService.getEntityMapper(name));
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type, String rootType) {
        return new MongoStorage<>(
                mongoClientManager.getCollection(name, mongoModelService.getEntityMapper(name).getEntityClass()),
                rootType, mongoModelService.getEntityMapper(name));
    }
}
