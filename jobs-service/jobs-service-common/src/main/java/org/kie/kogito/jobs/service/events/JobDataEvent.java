package org.kie.kogito.jobs.service.events;

import org.kie.kogito.event.AbstractDataEvent;
import org.kie.kogito.jobs.service.model.ScheduledJob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * <a href="https://cloudevents.io">CloudEvent</a> to propagate job status information from Job Service.
 */
public class JobDataEvent extends AbstractDataEvent<ScheduledJob> {

    public static final String JOB_EVENT_TYPE = "JobEvent";

    public JobDataEvent(String source, String identity, ScheduledJob data) {
        super(JOB_EVENT_TYPE,
                source,
                data,
                data.getProcessInstanceId(),
                data.getRootProcessInstanceId(),
                data.getProcessId(),
                data.getRootProcessId(),
                null,
                identity);
    }

    @JsonIgnore
    public static JobDataEventBuilder builder() {
        return new JobDataEventBuilder();
    }

    @JsonIgnoreType
    public static class JobDataEventBuilder {

        private String source;
        private ScheduledJob data;
        private String identity;

        public JobDataEventBuilder source(String source) {
            this.source = source;
            return this;
        }

        public JobDataEventBuilder identity(String identity) {
            this.identity = identity;
            return this;
        }

        public JobDataEventBuilder data(ScheduledJob data) {
            this.data = data;
            return this;
        }

        public JobDataEvent build() {
            return new JobDataEvent(source, identity, data);
        }
    }
}
