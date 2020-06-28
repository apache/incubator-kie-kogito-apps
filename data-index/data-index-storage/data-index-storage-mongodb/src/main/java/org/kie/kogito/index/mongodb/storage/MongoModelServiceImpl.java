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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.model.DomainEntityMapper;
import org.kie.kogito.index.mongodb.model.JobEntityMapper;
import org.kie.kogito.index.mongodb.model.ProcessIdEntity;
import org.kie.kogito.index.mongodb.model.ProcessIdEntityMapper;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntityMapper;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntityMapper;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.index.IndexCreateOrUpdateEvent;
import org.kie.kogito.persistence.mongodb.index.ProcessIndexEvent;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;
import org.kie.kogito.persistence.mongodb.storage.MongoModelService;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;

import static org.kie.kogito.index.Constants.JOBS_STORAGE;
import static org.kie.kogito.index.Constants.PROCESS_ID_MODEL_STORAGE;
import static org.kie.kogito.index.Constants.PROCESS_INSTANCES_STORAGE;
import static org.kie.kogito.index.Constants.USER_TASK_INSTANCES_STORAGE;
import static org.kie.kogito.index.mongodb.Constants.getDomainCollectionName;
import static org.kie.kogito.index.mongodb.Constants.isDomainCollection;
import static org.kie.kogito.persistence.mongodb.storage.StorageUtils.getCollection;

@ApplicationScoped
public class MongoModelServiceImpl implements MongoModelService {

    @Inject
    Event<IndexCreateOrUpdateEvent> indexCreateOrUpdateEvent;

    Map<String, Supplier<MongoEntityMapper>> entityMapperMap = new ConcurrentHashMap<>();

    {
        entityMapperMap.put(JOBS_STORAGE, JobEntityMapper::new);
        entityMapperMap.put(PROCESS_INSTANCES_STORAGE, ProcessInstanceEntityMapper::new);
        entityMapperMap.put(USER_TASK_INSTANCES_STORAGE, UserTaskInstanceEntityMapper::new);
        entityMapperMap.put(PROCESS_ID_MODEL_STORAGE, ProcessIdEntityMapper::new);
    }

    @PostConstruct
    void init() {
        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(PROCESS_INSTANCES_STORAGE, ProcessInstance.class.getName()));
        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(USER_TASK_INSTANCES_STORAGE, UserTaskInstance.class.getName()));
        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(JOBS_STORAGE, Job.class.getName()));
    }

    @Override
    public MongoEntityMapper getEntityMapper(String name) {
        Supplier<MongoEntityMapper> supplier = entityMapperMap.get(name);
        return Optional.ofNullable(supplier).map(Supplier::get).orElseGet(
                () -> isDomainCollection(name) ? new DomainEntityMapper() : null);
    }

    public void onProcessIndexEvent(@Observes ProcessIndexEvent event) {
        String processId = event.getProcessDescriptor().getProcessId();
        String processType = event.getProcessDescriptor().getProcessType();
        Storage<String, String> processIdStorage = new MongoStorage<>(getCollection(PROCESS_ID_MODEL_STORAGE, ProcessIdEntity.class),
                                                                      String.class.getName(), new ProcessIdEntityMapper());
        processIdStorage.put(processId, processType);

        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(getDomainCollectionName(processId), processType));
    }
}
