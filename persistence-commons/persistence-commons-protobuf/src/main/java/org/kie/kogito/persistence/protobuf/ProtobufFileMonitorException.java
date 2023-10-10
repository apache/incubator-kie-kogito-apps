package org.kie.kogito.persistence.protobuf;

class ProtobufFileMonitorException extends RuntimeException {

    ProtobufFileMonitorException(String message) {
        super(message);
    }

    ProtobufFileMonitorException(Throwable cause) {
        super(cause);
    }

    ProtobufFileMonitorException(String message, Throwable cause) {
        super(message, cause);
    }
}
