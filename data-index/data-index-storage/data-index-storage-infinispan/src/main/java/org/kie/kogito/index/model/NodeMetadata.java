package org.kie.kogito.index.model;

public class NodeMetadata {

    private String UniqueId;
    private String state;
    private String branch;
    private String action;

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "NodeMetadata{" +
                "UniqueId='" + UniqueId + '\'' +
                ", state='" + state + '\'' +
                ", branch='" + branch + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
