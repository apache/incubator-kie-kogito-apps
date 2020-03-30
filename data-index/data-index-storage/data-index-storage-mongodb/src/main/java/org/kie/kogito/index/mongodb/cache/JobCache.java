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

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.mongodb.model.JobEntity;
import org.kie.kogito.index.mongodb.query.JobQuery;
import org.kie.kogito.index.query.Query;

@ApplicationScoped
public class JobCache extends AbstractCache<String, Job> {

    @Inject
    JobQuery jobQuery;

    @Override
    public Query<Job> query() {
        return jobQuery;
    }

    @Override
    public String getRootType() {
        return Job.class.getName();
    }

    @Override
    public Job get(Object o) {
        return JobEntity.<JobEntity>findByIdOptional(o).map(JobEntity::toJob).orElse(null);
    }

    @Override
    public Job put(String s, Job job) {
        if (!s.equals(job.getId())) {
            throw new IllegalArgumentException("Job id doesn't match the key of the cache entry");
        }
        Job oldJob = this.get(s);
        Optional.ofNullable(JobEntity.fromJob(job)).ifPresent(entity -> entity.persistOrUpdate());
        Optional.ofNullable(oldJob).map(o -> this.objectUpdatedListener).orElseGet(() -> this.objectCreatedListener).ifPresent(l -> l.accept(job));
        return oldJob;
    }

    @Override
    public void clear() {
        JobEntity.deleteAll();
    }

    @Override
    public Job remove(Object o) {
        Job oldJob = this.get(o);
        Optional.ofNullable(oldJob).ifPresent(i ->JobEntity.deleteById(o));
        Optional.ofNullable(oldJob).flatMap(i -> this.objectRemovedListener).ifPresent(l -> l.accept((String) o));
        return oldJob;
    }

    @Override
    public int size() {
        return (int) JobEntity.count();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
}
