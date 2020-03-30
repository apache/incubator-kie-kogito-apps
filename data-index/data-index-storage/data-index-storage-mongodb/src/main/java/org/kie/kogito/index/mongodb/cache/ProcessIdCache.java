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

import org.kie.kogito.index.mongodb.model.ProcessIdEntity;
import org.kie.kogito.index.mongodb.query.ProcessIdQuery;
import org.kie.kogito.index.query.Query;

@ApplicationScoped
public class ProcessIdCache extends AbstractCache<String, String> {

    @Inject
    ProcessIdQuery processIdQuery;

    @Override
    public Query<String> query() {
        return processIdQuery;
    }

    @Override
    public String getRootType() {
        return String.class.getName();
    }

    @Override
    public String get(Object o) {
        return ProcessIdEntity.<ProcessIdEntity>findByIdOptional(o).map(e -> e.fullTypeName).orElse(null);
    }

    @Override
    public String put(String processId, String fullTypeName) {
        String oldType = this.get(processId);
        Optional.of(new ProcessIdEntity(processId, fullTypeName)).ifPresent(entity -> entity.persistOrUpdate());
        Optional.ofNullable(oldType).map(o -> this.objectUpdatedListener).orElseGet(() -> this.objectCreatedListener).ifPresent(l -> l.accept(fullTypeName));
        return oldType;
    }

    @Override
    public void clear() {
        ProcessIdEntity.deleteAll();
    }

    @Override
    public String remove(Object o) {
        String oldType = this.get(o);
        Optional.ofNullable(oldType).ifPresent(i -> ProcessIdEntity.deleteById(o));
        Optional.ofNullable(oldType).flatMap(i -> this.objectRemovedListener).ifPresent(l -> l.accept((String) o));
        return oldType;
    }

    @Override
    public int size() {
        return (int) ProcessIdEntity.count();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
}
