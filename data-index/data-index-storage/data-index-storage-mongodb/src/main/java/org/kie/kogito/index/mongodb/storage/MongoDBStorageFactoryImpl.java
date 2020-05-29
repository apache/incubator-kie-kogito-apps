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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Provider;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.index.IndexCreateOrUpdateEvent;
import org.kie.kogito.persistence.mongodb.index.ProcessIndexEvent;
import org.kie.kogito.persistence.mongodb.storage.MongoDBStorageFactory;

import static org.kie.kogito.index.Constants.JOBS_STORAGE;
import static org.kie.kogito.index.Constants.PROCESS_ID_MODEL_STORAGE;
import static org.kie.kogito.index.Constants.PROCESS_INSTANCES_STORAGE;
import static org.kie.kogito.index.Constants.USER_TASK_INSTANCES_STORAGE;
import static org.kie.kogito.index.mongodb.Constants.DOMAIN_COLLECTON_NAME_AFFIX;
import static org.kie.kogito.index.mongodb.Constants.getDomainCollectionName;

@ApplicationScoped
public class MongoDBStorageFactoryImpl implements MongoDBStorageFactory {

    @Inject
    ProcessInstanceStorage processInstanceCache;

    @Inject
    UserTaskInstanceStorage userTaskInstanceCache;

    @Inject
    JobStorage jobCache;

    @Inject
    ProcessIdStorage processIdCache;

    @Inject
    Provider<DomainStorage> domainModelCacheProvider;

    @Inject
    Event<IndexCreateOrUpdateEvent> indexCreateOrUpdateEvent;

    Map<String, DomainStorage> domainModelCacheMap = new ConcurrentHashMap<>();

    @Override
    public Storage<String, ?> getStorage(String name) {
        switch (name) {
            case PROCESS_INSTANCES_STORAGE:
                return processInstanceCache;
            case USER_TASK_INSTANCES_STORAGE:
                return userTaskInstanceCache;
            case JOBS_STORAGE:
                return jobCache;
            case PROCESS_ID_MODEL_STORAGE:
                return processIdCache;
            default:
                return null;
        }
    }

    @Override
    public Storage<String, ?> getOrCreateStorage(String name) {
        int index = name.lastIndexOf(DOMAIN_COLLECTON_NAME_AFFIX);
        if (index <= 0) {
            return null;
        }

        String processId = name.substring(0, index);
        DomainStorage cache = domainModelCacheMap.get(processId);
        if (cache == null) {
            cache = domainModelCacheProvider.get();
            cache.setProcessId(processId);
            domainModelCacheMap.putIfAbsent(processId, cache);
        }
        return cache;
    }

    public void onProcessIndexEvent(@Observes ProcessIndexEvent event) {
        String processId = event.getProcessDescriptor().getProcessId();
        String processType = event.getProcessDescriptor().getProcessType();
        processIdCache.put(processId, processType);

        DomainStorage domainStorage = (DomainStorage) this.getOrCreateStorage(getDomainCollectionName(processId));
        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(domainStorage.getCollection().getNamespace().getCollectionName(), processType));
    }
}
