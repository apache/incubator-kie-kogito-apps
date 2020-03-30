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

package org.kie.kogito.index.mongodb.cache;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntity;
import org.kie.kogito.index.mongodb.query.ProcessInstanceQuery;
import org.kie.kogito.index.query.Query;

@ApplicationScoped
public class ProcessInstanceCache extends AbstractCache<String, ProcessInstance> {

    @Inject
    ProcessInstanceQuery processInstanceQuery;

    @Override
    public Query<ProcessInstance> query() {
        return processInstanceQuery;
    }

    @Override
    public String getRootType() {
        return ProcessInstance.class.getName();
    }

    @Override
    public ProcessInstance get(Object o) {
        return ProcessInstanceEntity.<ProcessInstanceEntity>findByIdOptional(o).map(ProcessInstanceEntity::toProcessInstance).orElse(null);
    }

    @Override
    public ProcessInstance put(String s, ProcessInstance processInstance) {
        if (!s.equals(processInstance.getId())) {
            throw new IllegalArgumentException("Process instance id doesn't match the key of the cache entry");
        }
        ProcessInstance oldInstance = this.get(s);
        Optional.ofNullable(ProcessInstanceEntity.fromProcessInstance(processInstance)).ifPresent(entity -> entity.persistOrUpdate());
        Optional.ofNullable(oldInstance).map(o -> this.objectUpdatedListener).orElseGet(() -> this.objectCreatedListener).ifPresent(l -> l.accept(processInstance));
        return oldInstance;
    }

    @Override
    public void clear() {
        ProcessInstanceEntity.deleteAll();
    }

    @Override
    public ProcessInstance remove(Object o) {
        ProcessInstance oldInstance = this.get(o);
        Optional.ofNullable(oldInstance).ifPresent(i -> ProcessInstanceEntity.deleteById(o));
        Optional.ofNullable(oldInstance).flatMap(i -> this.objectRemovedListener).ifPresent(l -> l.accept((String) o));
        return oldInstance;
    }

    @Override
    public int size() {
        return (int) ProcessInstanceEntity.count();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
}
