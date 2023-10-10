package org.kie.kogito.persistence.api.proto;

import java.util.List;
import java.util.Objects;

public class DomainModelRegisteredEvent {

    private String processId;
    private DomainDescriptor domainDescriptor;
    private List<DomainDescriptor> additionalTypes;

    public DomainModelRegisteredEvent(String processId, DomainDescriptor domainDescriptor, List<DomainDescriptor> additionalTypes) {
        this.processId = processId;
        this.domainDescriptor = domainDescriptor;
        this.additionalTypes = additionalTypes;
    }

    public String getProcessId() {
        return processId;
    }

    public List<DomainDescriptor> getAdditionalTypes() {
        return additionalTypes;
    }

    public DomainDescriptor getDomainDescriptor() {
        return domainDescriptor;
    }

    @Override
    public String toString() {
        return "DomainModelRegisteredEvent{" +
                "processId='" + processId + '\'' +
                ", domainDescriptor=" + domainDescriptor +
                ", additionalTypes=" + additionalTypes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DomainModelRegisteredEvent that = (DomainModelRegisteredEvent) o;
        return Objects.equals(processId, that.processId) &&
                Objects.equals(domainDescriptor, that.domainDescriptor) &&
                Objects.equals(additionalTypes, that.additionalTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, domainDescriptor, additionalTypes);
    }
}
