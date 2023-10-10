package org.kie.kogito.jobs.service.exception;

public class JobValidationException extends RuntimeException {

    public JobValidationException(String message) {
        super(message);
    }

    public JobValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
