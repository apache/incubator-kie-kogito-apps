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
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.mongodb.model.JobEntity;

@ApplicationScoped
public class JobQuery extends AbstractEntityQuery<Job, JobEntity> {
    @Override
    PanacheQuery<JobEntity> queryWithSort(String queryString, Sort sort) {
        return JobEntity.find(queryString, sort);
    }

    @Override
    PanacheQuery<JobEntity> queryAllWithSort(Sort sort) {
        return JobEntity.findAll(sort);
    }

    @Override
    PanacheQuery<JobEntity> query(String queryString) {
        return JobEntity.find(queryString);
    }

    @Override
    PanacheQuery<JobEntity> queryAll() {
        return JobEntity.findAll();
    }

    @Override
    Function<JobEntity, Job> convertFunction() {
        return JobEntity::toJob;
    }
}
