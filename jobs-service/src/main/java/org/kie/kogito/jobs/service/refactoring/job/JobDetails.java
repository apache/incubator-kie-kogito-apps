/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kie.kogito.jobs.service.refactoring.job;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.StringJoiner;

import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.timer.Job;
import org.kie.kogito.timer.Trigger;

/**
 * Represents a Job Instance on the Job Service. This instance may be persisted and loaded at any point in time.
 * @param <T>
 */
public class JobDetails <T> {

    private String id;//the unique id internally on the job service
    private String correlationId; //the job id on the runtimes, for instance
    private JobStatus status;
    private ZonedDateTime lastUpdate;
    private Integer retries;
    private Integer priority;
    private Integer executionCounter;//number of times the job was executed
    //may be used to build the jobḦandle
    private String scheduledId;//the execution control on the scheduler (id on vertx.setTimer, quartzId...)

    private T payload;//process, rule, decision
    private Recipient recipient;//http callback, event topic
    private Trigger trigger;//when/how it should be executed
    private Type type;

    enum Type {

        HTTP(HttpJob.class);

        private Class<? extends Job> jobClass;

        Type(Class<? extends Job> jobClass) {
            this.jobClass = jobClass;
        }

        public Class<? extends Job> getJobClass() {
            return jobClass;
        }
    }

    public JobDetails(String id, String correlationId, JobStatus status, ZonedDateTime lastUpdate, Integer retries,
                      Integer executionCounter, String scheduledId, T payload, Recipient recipient, Trigger trigger,
                      Type type, Integer priority) {
        this.id = id;
        this.correlationId = correlationId;
        this.status = status;
        this.lastUpdate = lastUpdate;
        this.retries = retries;
        this.executionCounter = executionCounter;
        this.scheduledId = scheduledId;
        this.payload = payload;
        this.recipient = recipient;
        this.trigger = trigger;
        this.type = type;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public JobStatus getStatus() {
        return status;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Integer getRetries() {
        return retries;
    }

    public Integer getExecutionCounter() {
        return executionCounter;
    }

    public String getScheduledId() {
        return scheduledId;
    }

    public T getPayload() {
        return payload;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public Type getType() {
        return type;
    }

    public Integer getPriority() {
        return priority;
    }

    public static <T>JobDetailsBuilder<T> builder(){
        return new JobDetailsBuilder<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobDetails)) {
            return false;
        }
        JobDetails<?> that = (JobDetails<?>) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getCorrelationId(), that.getCorrelationId()) &&
                getStatus() == that.getStatus() &&
                Objects.equals(getLastUpdate(), that.getLastUpdate()) &&
                Objects.equals(getRetries(), that.getRetries()) &&
                Objects.equals(getExecutionCounter(), that.getExecutionCounter()) &&
                Objects.equals(getScheduledId(), that.getScheduledId()) &&
                Objects.equals(getPayload(), that.getPayload()) &&
                Objects.equals(getRecipient(), that.getRecipient()) &&
                Objects.equals(getTrigger(), that.getTrigger()) &&
                getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCorrelationId(), getStatus(), getLastUpdate(), getRetries(), getExecutionCounter(), getScheduledId(), getPayload(), getRecipient(), getTrigger(), getType());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JobDetails.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("correlationId='" + correlationId + "'")
                .add("status=" + status)
                .add("lastUpdate=" + lastUpdate)
                .add("retries=" + retries)
                .add("executionCounter=" + executionCounter)
                .add("scheduledId='" + scheduledId + "'")
                .add("payload=" + payload)
                .add("recipient=" + recipient)
                .add("trigger=" + trigger)
                .add("type=" + type)
                .toString();
    }
}
