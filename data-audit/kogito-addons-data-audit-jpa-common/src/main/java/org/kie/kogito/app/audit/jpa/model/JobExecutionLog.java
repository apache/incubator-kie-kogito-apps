/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.app.audit.jpa.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "JobExecutionLog")
@SequenceGenerator(name = "jobExecutionHistoryIdSeq", sequenceName = "JOB_EXECUTION_HISTORY_ID_SEQ")
public class JobExecutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "jobExecutionHistoryIdSeq")
    private Long id;

    @Column(name = "job_id")
    private String jobId;
    @Column(name = "correlation_id")
    private String correlationId;
    private String state;
    private String schedule;
    private String retry;
    @Column(name = "execution_timeout")
    private Long executionTimeout;

    @Column(name = "execution_timeout_unit")
    private String executionTimeoutUnit;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP")
    private Date timestamp;

    public JobExecutionLog() {
        this.timestamp = Timestamp.from(Instant.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "JobExecutionLog [id=" + id + ", jobId=" + jobId + ", correlationId=" + correlationId + ", state=" + state + ", schedule=" + schedule + ", retry=" + retry + ", executionTimeout="
                + executionTimeout + ", executionTimeoutUnit=" + executionTimeoutUnit + ", timestamp=" + timestamp + "]";
    }

}