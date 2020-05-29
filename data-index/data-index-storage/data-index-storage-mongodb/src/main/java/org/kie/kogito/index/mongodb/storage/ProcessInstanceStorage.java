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
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.persistence.mongodb.index.IndexCreateOrUpdateEvent;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntity;
import org.kie.kogito.index.mongodb.query.ProcessInstanceQuery;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.mongodb.storage.AbstractStorage;

@ApplicationScoped
public class ProcessInstanceStorage extends AbstractStorage<String, ProcessInstance, ProcessInstanceEntity> {

    @Inject
    Provider<ProcessInstanceQuery> processInstanceQueryProvider;

    @Inject
    Event<IndexCreateOrUpdateEvent> indexCreateOrUpdateEvent;

    @PostConstruct
    public void init() {
        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(this.getCollection().getNamespace().getCollectionName(), ProcessInstance.class.getName()));
    }

    @Override
    public MongoCollection<ProcessInstanceEntity> getCollection() {
        return MongoOperations.mongoCollection(ProcessInstanceEntity.class);
    }

    @Override
    protected ProcessInstanceEntity mapToEntity(String key, ProcessInstance value) {
        return ProcessInstanceEntity.fromProcessInstance(value);
    }

    @Override
    protected ProcessInstance mapToModel(String key, ProcessInstanceEntity entity) {
        return ProcessInstanceEntity.toProcessInstance(entity);
    }

    @Override
    public Query<ProcessInstance> query() {
        return processInstanceQueryProvider.get();
    }

    @Override
    public String getRootType() {
        return ProcessInstance.class.getName();
    }
}
