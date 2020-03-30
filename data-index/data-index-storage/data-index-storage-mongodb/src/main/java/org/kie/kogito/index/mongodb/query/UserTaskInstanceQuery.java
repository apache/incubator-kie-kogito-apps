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

package org.kie.kogito.index.mongodb.query;

import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntity;

@ApplicationScoped
public class UserTaskInstanceQuery extends AbstractEntityQuery<UserTaskInstance, UserTaskInstanceEntity> {
    @Override
    PanacheQuery<UserTaskInstanceEntity> queryWithSort(String queryString, Sort sort) {
        return UserTaskInstanceEntity.find(queryString, sort);
    }

    @Override
    PanacheQuery<UserTaskInstanceEntity> queryAllWithSort(Sort sort) {
        return UserTaskInstanceEntity.findAll(sort);
    }

    @Override
    PanacheQuery<UserTaskInstanceEntity> query(String queryString) {
        return UserTaskInstanceEntity.find(queryString);
    }

    @Override
    PanacheQuery<UserTaskInstanceEntity> queryAll() {
        return UserTaskInstanceEntity.findAll();
    }

    @Override
    Function<UserTaskInstanceEntity, UserTaskInstance> convertFunction() {
        return UserTaskInstanceEntity::toUserTaskInstance;
    }
}
