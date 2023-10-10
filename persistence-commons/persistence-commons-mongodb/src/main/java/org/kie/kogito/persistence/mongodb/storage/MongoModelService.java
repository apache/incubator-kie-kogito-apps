package org.kie.kogito.persistence.mongodb.storage;

import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;

/**
 * This service manages the entity mappers which are responsible for converting between data models and mongo storage entities
 */
public interface MongoModelService {

    /**
     * Get the entity mapper which converts between the given data model and the corresponding mongo storage entity
     * 
     * @param name the name of the data model
     * @return the entity mapper for the given data model
     */
    <V, E> MongoEntityMapper<V, E> getEntityMapper(String name);
}
