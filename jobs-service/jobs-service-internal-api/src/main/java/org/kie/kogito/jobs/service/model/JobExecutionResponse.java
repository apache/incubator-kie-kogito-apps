package org.kie.kogito.jobs.service.model;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class JobExecutionResponse {

    private String message;
    private String code;
    private ZonedDateTime timestamp;
    private String jobId;

    public JobExecutionResponse() {
    }

    public JobExecutionResponse(String message, String code, ZonedDateTime timestamp, String jobId) {
        this.message = message;
        this.code = code;
        this.timestamp = timestamp;
        this.jobId = jobId;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getJobId() {
        return jobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobExecutionResponse)) {
            return false;
        }
        JobExecutionResponse that = (JobExecutionResponse) o;
        return Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getTimestamp(), that.getTimestamp()) &&
                Objects.equals(getJobId(), that.getJobId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getCode(), getTimestamp(), getJobId());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JobExecutionResponse.class.getSimpleName() + "[", "]")
                .add("message='" + message + "'")
                .add("code='" + code + "'")
                .add("timestamp=" + timestamp)
                .add("jobId='" + jobId + "'")
                .toString();
    }

    public static JobExecutionResponseBuilder builder() {
        return new JobExecutionResponseBuilder();
    }

    public static class JobExecutionResponseBuilder {

        private String message;
        private String code;
        private ZonedDateTime timestamp;
        private String jobId;

        public JobExecutionResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public JobExecutionResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public JobExecutionResponseBuilder timestamp(ZonedDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public JobExecutionResponseBuilder now() {
            this.timestamp = ZonedDateTime.now();
            return this;
        }

        public JobExecutionResponseBuilder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public JobExecutionResponse build() {
            return new JobExecutionResponse(message, code, timestamp, jobId);
        }
    }
}
