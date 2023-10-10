package org.kie.kogito.index.model;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserTaskInstance extends UserTaskInstanceMeta {

    private String processId;
    private String rootProcessId;
    private String rootProcessInstanceId;
    private ObjectNode inputs;
    private ObjectNode outputs;
    private String endpoint;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String id) {
        if (id != null && !id.trim().isEmpty()) {
            this.processId = id;
        }
    }

    public String getRootProcessId() {
        return rootProcessId;
    }

    public void setRootProcessId(String id) {
        if (id != null && !id.trim().isEmpty()) {
            this.rootProcessId = id;
        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return "UserTaskInstance{" +
                "processId='" + processId + '\'' +
                ", rootProcessId='" + rootProcessId + '\'' +
                ", rootProcessInstanceId='" + rootProcessInstanceId + '\'' +
                ", inputs=" + inputs +
                ", outputs=" + outputs +
                ", endpoint='" + endpoint + '\'' +
                "} " + super.toString();
    }

    public String getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }

    public void setRootProcessInstanceId(String id) {
        if (id != null && !id.trim().isEmpty()) {
            this.rootProcessInstanceId = id;
        }
    }

    public ObjectNode getInputs() {
        return inputs;
    }

    public void setInputs(ObjectNode inputs) {
        this.inputs = inputs;
    }

    public ObjectNode getOutputs() {
        return outputs;
    }

    public void setOutputs(ObjectNode outputs) {
        this.outputs = outputs;
    }
}
