package org.kie.kogito.jobs.service.stream;

public final class AvailableStreams {

    public static final String JOB_ERROR = "job-error";
    public static final String JOB_ERROR_EVENTS = "job-error-events";
    public static final String JOB_SUCCESS = "job-success";
    public static final String JOB_SUCCESS_EVENTS = "job-success-events";
    public static final String JOB_STATUS_CHANGE = "job-status-change";
    public static final String JOB_STATUS_CHANGE_EVENTS = "job-status-change-events";
    public static final String JOB_STATUS_CHANGE_EVENTS_TOPIC = "kogito-job-service-job-status-events";

    private AvailableStreams() {

    }
}
