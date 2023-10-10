package org.kie.kogito.index.service;

public class DataIndexServiceException extends RuntimeException {

    public DataIndexServiceException(String message) {
        super(message);
    }

    public DataIndexServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
