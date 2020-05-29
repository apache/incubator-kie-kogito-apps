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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.mongodb.client.MongoCollection;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.storage.UserTaskInstanceStorage;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntity;
import org.kie.kogito.persistence.mongodb.query.AbstractQuery;

@Dependent
public class UserTaskInstanceQuery extends AbstractQuery<UserTaskInstance, UserTaskInstanceEntity> {

    @Inject
    UserTaskInstanceStorage userTaskInstanceStorage;

    @Override
    protected MongoCollection<UserTaskInstanceEntity> getCollection() {
        return userTaskInstanceStorage.getCollection();
    }

    @Override
    protected UserTaskInstance mapToModel(UserTaskInstanceEntity userTaskInstanceEntity) {
        return UserTaskInstanceEntity.toUserTaskInstance(userTaskInstanceEntity);
    }
}
