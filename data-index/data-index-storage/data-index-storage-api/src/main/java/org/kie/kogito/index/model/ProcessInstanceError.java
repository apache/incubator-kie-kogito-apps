package org.kie.kogito.index.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessInstanceError {

    private String nodeDefinitionId;
    @JsonProperty("errorMessage")
    private String message;

    public ProcessInstanceError() {
    }

    public ProcessInstanceError(String nodeDefinitionId, String message) {
        this.nodeDefinitionId = nodeDefinitionId;
        this.message = message;
    }

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
    public String toString() {
        return "ProcessInstanceError{" +
                "nodeDefinitionId='" + nodeDefinitionId + '\'' +
                ", message='" + message + '\'' +
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
        ProcessInstanceError that = (ProcessInstanceError) o;
        return Objects.equals(nodeDefinitionId, that.nodeDefinitionId) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeDefinitionId, message);
    }
}
