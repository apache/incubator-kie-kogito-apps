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

package org.kie.kogito.persistence.mongodb.model;

import io.quarkus.mongodb.panache.runtime.MongoOperations;

/**
 * A mongo entity mapper is responsible for converting between a data model and the corresponding mongo storage entity
 * @param <K> the type of the data model key
 * @param <V> the type of the data model
 * @param <E> the type of the mongo storage entity
 */
public interface MongoEntityMapper<K, V, E> {

    /**
     * Get the mongo storage entity class
     * @return the mongo storage entity class
     */
    Class<E> getEntityClass();

    /**
     * Convert data model to mongo storage entity
     * @param key the key of the data model
     * @param value the data model
     * @return the converted mongo storage entity
     */
    E mapToEntity(K key, V value);

    /**
     * Convert mongo storage entity to data model
     * @param entity the mongo storage entity
     * @return the converted data model
     */
    V mapToModel(E entity);

    /**
     * Convert the data model attribute name to mongo storage attribute name
     * @param attribute the data model attribute name
     * @return the corresponding mongo storage attribute name
     */
    default String convertAttribute(String attribute) {
        return "id".equalsIgnoreCase(attribute) ? MongoOperations.ID : attribute;
    }
}
