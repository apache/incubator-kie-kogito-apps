package org.kie.kogito.persistence.protobuf;

public class ProtobufValidationException extends Exception {

    public ProtobufValidationException() {
    }

    public ProtobufValidationException(String message) {
        super(message);
    }

    public ProtobufValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
