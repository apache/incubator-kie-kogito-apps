/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.index.oracle.storage;

import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.function.Function;

import org.kie.kogito.index.oracle.model.AbstractEntity;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.Query;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Multi;

import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import static java.util.stream.Collectors.toMap;

public abstract class AbstractStorage<E extends AbstractEntity, V> implements Storage<String, V> {

    private static final String LISTENER_NOT_AVAILABLE_IN_ORACLE_SQL = "Listener not available in OracleSQL";

    private PanacheRepositoryBase<E, String> repository;
    private Class<V> modelClass;
    private Class<E> entityClass;
    private Function<E, V> mapToModel;
    private Function<V, E> mapToEntity;

    private Function<E, String> mapEntityToKey;

    protected AbstractStorage() {
    }

    protected AbstractStorage(PanacheRepositoryBase<E, String> repository, Class<V> modelClass, Class<E> entityClass, Function<E, V> mapToModel,
            Function<V, E> mapToEntity, Function<E, String> mapEntityToKey) {
        this.repository = repository;
        this.modelClass = modelClass;
        this.mapToModel = mapToModel;
        this.mapToEntity = mapToEntity;
        this.entityClass = entityClass;
        this.mapEntityToKey = mapEntityToKey;
    }

    @Override
    public Multi<V> objectCreatedListener() {
        throw new UnsupportedOperationException(LISTENER_NOT_AVAILABLE_IN_ORACLE_SQL);
    }

    @Override
    public Multi<V> objectUpdatedListener() {
        throw new UnsupportedOperationException(LISTENER_NOT_AVAILABLE_IN_ORACLE_SQL);
    }

    @Override
    public Multi<String> objectRemovedListener() {
        throw new UnsupportedOperationException(LISTENER_NOT_AVAILABLE_IN_ORACLE_SQL);
    }

    @Override
    public Query<V> query() {
        return new OracleQuery<>(repository, mapToModel, entityClass);
    }

    @Override
    @Transactional
    public V get(String key) {
        return repository.findByIdOptional(key).map(mapToModel).orElse(null);
    }

    @Override
    @Transactional
    public V put(String key, V value) {
        //Pessimistic lock is used to lock the row to handle concurrency with an exiting registry
        E persistedEntity = repository.findById(key, LockModeType.PESSIMISTIC_WRITE);
        E newEntity = mapToEntity.apply(value);
        if (persistedEntity != null) {
            repository.getEntityManager().merge(newEntity);
        } else {
            try {
                //to handle concurrency in case of a new registry persist flush and throw an exception to allow retry on the caller side
                repository.persistAndFlush(newEntity);
            } catch (PersistenceException e) {
                throw new ConcurrentModificationException(e);
            }
        }
        return value;
    }

    @Override
    @Transactional
    public V remove(String key) {
        V value = get(key);
        if (value != null) {
            repository.deleteById(key);
        }
        return value;
    }

    @Transactional
    @Override
    public boolean containsKey(String key) {
        return repository.count("id = ?1", key) == 1;
    }

    @Override
    public Map<String, V> entries() {
        return repository.streamAll().collect(toMap(mapEntityToKey, mapToModel));
    }

    @Override
    @Transactional
    public void clear() {
        repository.deleteAll();
    }

    @Override
    public String getRootType() {
        return modelClass.getCanonicalName();
    }

    protected PanacheRepositoryBase<E, String> getRepository() {
        return repository;
    }
}
