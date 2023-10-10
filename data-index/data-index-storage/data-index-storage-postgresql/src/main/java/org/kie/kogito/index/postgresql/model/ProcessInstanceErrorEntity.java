package org.kie.kogito.index.postgresql.model;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class ProcessInstanceErrorEntity {

    private String nodeDefinitionId;
    private String message;

    public String getNodeDefinitionId() {
        return nodeDefinitionId;
    }

    public void setNodeDefinitionId(String nodeDefinitionId) {
        this.nodeDefinitionId = nodeDefinitionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessInstanceErrorEntity that = (ProcessInstanceErrorEntity) o;
        return Objects.equals(nodeDefinitionId, that.nodeDefinitionId) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeDefinitionId, message);
    }

}
