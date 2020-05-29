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

package org.kie.kogito.index.mongodb.storage;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Provider;

import com.mongodb.client.MongoCollection;
import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.kie.kogito.index.mongodb.model.ProcessIdEntity;
import org.kie.kogito.index.mongodb.query.ProcessIdQuery;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.mongodb.index.IndexCreateOrUpdateEvent;
import org.kie.kogito.persistence.mongodb.storage.AbstractStorage;

@ApplicationScoped
public class ProcessIdStorage extends AbstractStorage<String, String, ProcessIdEntity> {

    @Inject
    Provider<ProcessIdQuery> processIdQueryProvider;

    @Inject
    Event<IndexCreateOrUpdateEvent> indexCreateOrUpdateEvent;

    @PostConstruct
    public void init() {
        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(this.getCollection().getNamespace().getCollectionName(), String.class.getName()));
    }

    @Override
    public MongoCollection<ProcessIdEntity> getCollection() {
        return MongoOperations.mongoCollection(ProcessIdEntity.class);
    }

    @Override
    protected ProcessIdEntity mapToEntity(String key, String value) {
        return ProcessIdEntity.fromProcess(key, value);
    }

    @Override
    protected String mapToModel(String key, ProcessIdEntity entity) {
        return entity.fullTypeName;
    }

    @Override
    public Query<String> query() {
        return processIdQueryProvider.get();
    }

    @Override
    public String getRootType() {
        return String.class.getName();
    }
}
