/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.app.audit.graphql.type;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JobExecutionTO {

    private String jobId;
    private String correlationId;
    private String state;
    private String schedule;
    private String retry;
    private Long executionTimeout;
    private String executionTimeoutUnit;
    private OffsetDateTime timestamp;

    public JobExecutionTO() {

    }

    public JobExecutionTO(String jobId, String correlationId, String state, String schedule, String retry, Long executionTimeout, String executionTimeoutUnit, Date timestamp) {
        this.jobId = jobId;
        this.correlationId = correlationId;
        this.state = state;
        this.schedule = schedule;
        this.retry = retry;
        this.executionTimeout = executionTimeout;
        this.executionTimeoutUnit = executionTimeoutUnit;
        this.timestamp = OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("UTC"));
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRetry() {
        return retry;
    }

    public void setRetry(String retry) {
        this.retry = retry;
    }

    public Long getExecutionTimeout() {
        return executionTimeout;
    }

    public void setExecutionTimeout(Long executionTimeout) {
        this.executionTimeout = executionTimeout;
    }

    public String getExecutionTimeoutUnit() {
        return executionTimeoutUnit;
    }

    public void setExecutionTimeoutUnit(String executionTimeoutUnit) {
        this.executionTimeoutUnit = executionTimeoutUnit;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
