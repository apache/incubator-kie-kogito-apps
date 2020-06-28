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

import java.util.Objects;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;

import static org.kie.kogito.index.Constants.JOBS_STORAGE;

@MongoEntity(collection = JOBS_STORAGE)
public class JobEntity extends PanacheMongoEntityBase {

    @BsonId
    public String id;

    public String processId;

    public String processInstanceId;

    public String rootProcessId;

    public String rootProcessInstanceId;

    public Long expirationTime;

    public Integer priority;

    public String callbackEndpoint;

    public Long repeatInterval;

    public Integer repeatLimit;

    public String scheduledId;

    public Integer retries;

    public String status;

    public Long lastUpdate;

    public Integer executionCounter;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobEntity jobEntity = (JobEntity) o;
        return Objects.equals(id, jobEntity.id) &&
                Objects.equals(processId, jobEntity.processId) &&
                Objects.equals(processInstanceId, jobEntity.processInstanceId) &&
                Objects.equals(rootProcessId, jobEntity.rootProcessId) &&
                Objects.equals(rootProcessInstanceId, jobEntity.rootProcessInstanceId) &&
                Objects.equals(expirationTime, jobEntity.expirationTime) &&
                Objects.equals(priority, jobEntity.priority) &&
                Objects.equals(callbackEndpoint, jobEntity.callbackEndpoint) &&
                Objects.equals(repeatInterval, jobEntity.repeatInterval) &&
                Objects.equals(repeatLimit, jobEntity.repeatLimit) &&
                Objects.equals(scheduledId, jobEntity.scheduledId) &&
                Objects.equals(retries, jobEntity.retries) &&
                Objects.equals(status, jobEntity.status) &&
                Objects.equals(lastUpdate, jobEntity.lastUpdate) &&
                Objects.equals(executionCounter, jobEntity.executionCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, processId, processInstanceId, rootProcessId, rootProcessInstanceId, expirationTime, priority, callbackEndpoint, repeatInterval, repeatLimit, scheduledId, retries, status, lastUpdate, executionCounter);
    }
}
