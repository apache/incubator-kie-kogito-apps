package org.kie.kogito.persistence.mongodb.index;

import java.util.Objects;

import org.kie.kogito.persistence.api.schema.ProcessDescriptor;

public class ProcessIndexEvent {

    ProcessDescriptor processDescriptor;

    public ProcessIndexEvent(ProcessDescriptor processDescriptor) {
        this.processDescriptor = processDescriptor;
    }

    public ProcessDescriptor getProcessDescriptor() {
        return processDescriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessIndexEvent that = (ProcessIndexEvent) o;
        return Objects.equals(processDescriptor, that.processDescriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processDescriptor);
    }
}
