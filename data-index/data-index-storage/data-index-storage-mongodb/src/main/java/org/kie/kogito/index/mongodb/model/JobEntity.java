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

import org.bson.codecs.pojo.annotations.BsonId;

public class JobEntity {

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
        return Objects.equals(id, jobEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
