package org.kie.kogito.index.oracle.model;

import java.io.Serializable;
import java.util.Objects;

public class MilestoneEntityId implements Serializable {

    private String id;
    private String processInstance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(String processInstance) {
        this.processInstance = processInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MilestoneEntityId that = (MilestoneEntityId) o;
        return id.equals(that.id) && processInstance.equals(that.processInstance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, processInstance);
    }

    @Override
    public String toString() {
        return "MilestoneEntityId{" +
                "id='" + id + '\'' +
                ", processInstance='" + processInstance + '\'' +
                '}';
    }
}
