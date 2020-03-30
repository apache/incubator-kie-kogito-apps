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

import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntity;
import org.kie.kogito.index.mongodb.query.UserTaskInstanceQuery;
import org.kie.kogito.index.query.Query;

@ApplicationScoped
public class UserTaskInstanceCache extends AbstractCache<String, UserTaskInstance> {

    @Inject
    UserTaskInstanceQuery userTaskInstanceQuery;

    @Override
    public Query<UserTaskInstance> query() {
        return userTaskInstanceQuery;
    }

    @Override
    public String getRootType() {
        return UserTaskInstance.class.getName();
    }

    @Override
    public UserTaskInstance get(Object o) {
        return UserTaskInstanceEntity.<UserTaskInstanceEntity>findByIdOptional(o).map(UserTaskInstanceEntity::toUserTaskInstance).orElse(null);
    }

    @Override
    public UserTaskInstance put(String s, UserTaskInstance userTaskInstance) {
        if (!s.equals(userTaskInstance.getId())) {
            throw new IllegalArgumentException("User task instance id doesn't match the key of the cache entry");
        }
        UserTaskInstance oldInstance = this.get(s);
        Optional.ofNullable(UserTaskInstanceEntity.fromUserTaskInstance(userTaskInstance)).ifPresent(entity -> entity.persistOrUpdate());
        Optional.ofNullable(oldInstance).map(o -> this.objectUpdatedListener).orElseGet(() -> this.objectCreatedListener).ifPresent(l -> l.accept(userTaskInstance));
        return oldInstance;
    }

    @Override
    public void clear() {
        UserTaskInstanceEntity.deleteAll();
    }

    @Override
    public UserTaskInstance remove(Object o) {
        UserTaskInstance oldInstance = this.get(o);
        Optional.ofNullable(oldInstance).ifPresent(i -> UserTaskInstanceEntity.deleteById(o));
        Optional.ofNullable(oldInstance).flatMap(i -> this.objectRemovedListener).ifPresent(l -> l.accept((String) o));
        return oldInstance;
    }

    @Override
    public int size() {
        return (int) UserTaskInstanceEntity.count();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
}
