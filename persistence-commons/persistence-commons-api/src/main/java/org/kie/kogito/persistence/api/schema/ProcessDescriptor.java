package org.kie.kogito.persistence.api.schema;

import java.util.Objects;

public class ProcessDescriptor {

    String processId;

    String processType;

    public ProcessDescriptor(String processId, String processType) {
        this.processId = processId;
        this.processType = processType;
    }

    public String getProcessId() {
        return processId;
    }

    public String getProcessType() {
        return processType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessDescriptor that = (ProcessDescriptor) o;
        return Objects.equals(processId, that.processId) &&
                Objects.equals(processType, that.processType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, processType);
    }

    @Override
    public String toString() {
        return "ProcessDescriptor{" +
                "processId='" + processId + '\'' +
                ", processType='" + processType + '\'' +
                '}';
    }
}
