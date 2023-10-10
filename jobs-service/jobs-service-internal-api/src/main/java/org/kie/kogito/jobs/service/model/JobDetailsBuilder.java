package org.kie.kogito.jobs.service.model;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.kie.kogito.timer.Trigger;

public class JobDetailsBuilder {

    private String id;
    private String correlationId;
    private JobStatus status;
    private ZonedDateTime lastUpdate;
    private Integer retries = 0;
    private Integer executionCounter = 0;
    private String scheduledId;
    private Recipient recipient;
    private Trigger trigger;
    private Integer priority;
    private Long executionTimeout;
    private ChronoUnit executionTimeoutUnit;

    public JobDetailsBuilder id(String id) {
        this.id = id;
        return this;
    }

    public JobDetailsBuilder correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public JobDetailsBuilder status(JobStatus status) {
        this.status = status;
        return this;
    }

    public JobDetailsBuilder lastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public JobDetailsBuilder retries(Integer retries) {
        this.retries = retries;
        return this;
    }

    public JobDetailsBuilder executionCounter(Integer executionCounter) {
        this.executionCounter = executionCounter;
        return this;
    }

    public JobDetailsBuilder scheduledId(String scheduledId) {
        this.scheduledId = scheduledId;
        return this;
    }

    public JobDetailsBuilder recipient(Recipient recipient) {
        this.recipient = recipient;
        return this;
    }

    public JobDetailsBuilder trigger(Trigger trigger) {
        this.trigger = trigger;
        return this;
    }

    public JobDetailsBuilder priority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public JobDetailsBuilder executionTimeout(Long executionTimeout) {
        this.executionTimeout = executionTimeout;
        return this;
    }

    public JobDetailsBuilder executionTimeoutUnit(ChronoUnit executionTimeoutUnit) {
        this.executionTimeoutUnit = executionTimeoutUnit;
        return this;
    }

    public JobDetails build() {
        return new JobDetails(id, correlationId, status, lastUpdate, retries, executionCounter, scheduledId,
                recipient, trigger, priority, executionTimeout, executionTimeoutUnit);
    }

    public JobDetailsBuilder of(JobDetails jobDetails) {
        return id(jobDetails.getId())
                .correlationId(jobDetails.getCorrelationId())
                .status(jobDetails.getStatus())
                .lastUpdate(jobDetails.getLastUpdate())
                .retries(jobDetails.getRetries())
                .executionCounter(jobDetails.getExecutionCounter())
                .scheduledId(jobDetails.getScheduledId())
                .recipient(jobDetails.getRecipient())
                .trigger(jobDetails.getTrigger())
                .priority(jobDetails.getPriority())
                .executionTimeout(jobDetails.getExecutionTimeout())
                .executionTimeoutUnit(jobDetails.getExecutionTimeoutUnit());
    }

    public JobDetailsBuilder incrementRetries() {
        this.retries++;
        return this;
    }

    public JobDetailsBuilder incrementExecutionCounter() {
        this.executionCounter++;
        return this;
    }

    public JobDetailsBuilder merge(JobDetails jobDetails) {
        final Optional<JobDetails> j = Optional.ofNullable(jobDetails);
        return scheduledId(j.map(JobDetails::getScheduledId).orElse(scheduledId))
                .retries(j.map(JobDetails::getRetries).orElse(retries))
                .status(j.map(JobDetails::getStatus).orElse(status))
                .id(j.map(JobDetails::getId).orElse(id))
                .trigger(j.map(JobDetails::getTrigger).orElse(trigger))
                .recipient(j.map(JobDetails::getRecipient).orElse(recipient))
                .correlationId(j.map(JobDetails::getCorrelationId).orElse(correlationId))
                .priority(j.map(JobDetails::getPriority).orElse(priority))
                .executionCounter(j.map(JobDetails::getExecutionCounter).orElse(executionCounter))
                .executionTimeout(j.map(JobDetails::getExecutionTimeout).orElse(executionTimeout))
                .executionTimeoutUnit(j.map(JobDetails::getExecutionTimeoutUnit).orElse(executionTimeoutUnit));
    }
}