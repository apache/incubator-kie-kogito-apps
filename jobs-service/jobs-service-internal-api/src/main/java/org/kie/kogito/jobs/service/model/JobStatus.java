package org.kie.kogito.jobs.service.model;

public enum JobStatus {
    ERROR, //final
    EXECUTED, //final
    SCHEDULED, //active
    RETRY, //active
    CANCELED//final
}
