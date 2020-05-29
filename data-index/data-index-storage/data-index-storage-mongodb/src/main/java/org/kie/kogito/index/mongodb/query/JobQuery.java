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
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.mongodb.storage.JobStorage;
import org.kie.kogito.index.mongodb.model.JobEntity;
import org.kie.kogito.persistence.mongodb.query.AbstractQuery;

@Dependent
public class JobQuery extends AbstractQuery<Job, JobEntity> {

    @Inject
    JobStorage jobStorage;

    @Override
    protected MongoCollection<JobEntity> getCollection() {
        return jobStorage.getCollection();
    }

    @Override
    protected Job mapToModel(JobEntity entity) {
        return JobEntity.toJob(entity);
    }
}
