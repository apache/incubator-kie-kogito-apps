package org.kie.kogito.jobs.service.exception;

public class JobExecutionException extends JobServiceException {

    private String jobId;

    public JobExecutionException(String jobId, String message) {
        super(message);
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }
}