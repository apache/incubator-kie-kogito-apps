package org.kie.kogito.jobs.service.exception;

public class JobServiceException extends RuntimeException {

    public JobServiceException(String message) {
        super(message);
    }

    public JobServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
