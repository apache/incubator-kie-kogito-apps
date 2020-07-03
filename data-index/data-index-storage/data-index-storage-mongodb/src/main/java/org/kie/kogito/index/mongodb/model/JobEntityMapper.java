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

package org.kie.kogito.index.mongodb.model;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;

import static org.kie.kogito.persistence.mongodb.model.ModelUtils.instantToZonedDateTime;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.zonedDateTimeToInstant;

public class JobEntityMapper implements MongoEntityMapper<Job, JobEntity> {

    @Override
    public Class<JobEntity> getEntityClass() {
        return JobEntity.class;
    }

    @Override
    public JobEntity mapToEntity(String key, Job job) {
        if (job == null) {
            return null;
        }

        JobEntity entity = new JobEntity();
        entity.id = job.getId();
        entity.processId = job.getProcessId();
        entity.processInstanceId = job.getProcessInstanceId();
        entity.rootProcessId = job.getRootProcessId();
        entity.rootProcessInstanceId = job.getRootProcessInstanceId();
        entity.expirationTime = zonedDateTimeToInstant(job.getExpirationTime());
        entity.priority = job.getPriority();
        entity.callbackEndpoint = job.getCallbackEndpoint();
        entity.repeatInterval = job.getRepeatInterval();
        entity.repeatLimit = job.getRepeatLimit();
        entity.scheduledId = job.getScheduledId();
        entity.retries = job.getRetries();
        entity.status = job.getStatus();
        entity.lastUpdate = zonedDateTimeToInstant(job.getLastUpdate());
        entity.executionCounter = job.getExecutionCounter();
        return entity;
    }

    @Override
    public Job mapToModel(JobEntity entity) {
        if (entity == null) {
            return null;
        }

        Job job = new Job();
        job.setId(entity.id);
        job.setProcessId(entity.processId);
        job.setProcessInstanceId(entity.processInstanceId);
        job.setRootProcessId(entity.rootProcessId);
        job.setRootProcessInstanceId(entity.rootProcessInstanceId);
        job.setExpirationTime(instantToZonedDateTime(entity.expirationTime));
        job.setPriority(entity.priority);
        job.setCallbackEndpoint(entity.callbackEndpoint);
        job.setRepeatInterval(entity.repeatInterval);
        job.setRepeatLimit(entity.repeatLimit);
        job.setScheduledId(entity.scheduledId);
        job.setRetries(entity.retries);
        job.setStatus(entity.status);
        job.setLastUpdate(instantToZonedDateTime(entity.lastUpdate));
        job.setExecutionCounter(entity.executionCounter);
        return job;
    }
}
