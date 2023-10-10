package org.kie.kogito.jobs.service.validation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.api.schedule.timer.TimerSchedule;
import org.kie.kogito.jobs.service.exception.JobValidationException;

@ApplicationScoped
public class JobValidator {

    private final RecipientValidatorProvider recipientValidatorProvider;

    @Inject
    public JobValidator(RecipientValidatorProvider recipientValidatorProvider) {
        this.recipientValidatorProvider = recipientValidatorProvider;
    }

    public void validateToCreate(Job job) {
        if (StringUtils.isEmpty(job.getId())) {
            throw new JobValidationException("A non empty id must be provided to create a Job.");
        }
        if (StringUtils.isEmpty(job.getCorrelationId())) {
            throw new JobValidationException("A non empty correlationId id must be provided to create a Job.");
        }
        if (job.getSchedule() == null) {
            throw new JobValidationException("A non null Schedule must be provided to create a Job.");
        }
        if (!(job.getSchedule() instanceof TimerSchedule)) {
            throw new JobValidationException("Only the TimerSchedule is supported at this moment, but is: " + job.getSchedule().getClass() + ".");
        }
        validateToCreate((TimerSchedule) job.getSchedule());
        if (job.getRecipient() == null) {
            throw new JobValidationException("A non null Recipient must be provided to create a Job.");
        }
        if (job.getExecutionTimeout() != null && job.getExecutionTimeout() < 0) {
            throw new JobValidationException("Job executionTimeout can not be negative, but is: " + job.getExecutionTimeout() + ".");
        }
        recipientValidatorProvider.getValidator(job.getRecipient())
                .ifPresent(validator -> validator.validate(job.getRecipient(), new ValidatorContext(job)));
    }

    private void validateToCreate(TimerSchedule schedule) {
        if (schedule.getStartTime() == null) {
            throw new JobValidationException("A non null startTime must be provided to create a Job with a TimerSchedule.");
        }
        if (schedule.getRepeatCount() != null && schedule.getRepeatCount() < 0) {
            throw new JobValidationException("A negative repeatCount is not supported to create Job with a TimerSchedule, but is: " + schedule.getRepeatCount() + ".");
        }
        if (schedule.getDelay() != null && schedule.getDelay() < 0) {
            throw new JobValidationException("A negative delay is not supported to create Job with a TimerSchedule, but is: " + schedule.getDelay() + ".");
        }
    }
}
