package org.kie.kogito.index.model;

import java.time.ZonedDateTime;
import java.util.List;

public class KogitoMetadata {

    private ZonedDateTime lastUpdate;
    private List<ProcessInstanceMeta> processInstances;
    private List<UserTaskInstanceMeta> userTasks;

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<ProcessInstanceMeta> getProcessInstances() {
        return processInstances;
    }

    public void setProcessInstances(List<ProcessInstanceMeta> processInstances) {
        this.processInstances = processInstances;
    }

    public List<UserTaskInstanceMeta> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(List<UserTaskInstanceMeta> userTasks) {
        this.userTasks = userTasks;
    }

    @Override
    public String toString() {
        return "KogitoMeta{" +
                "lastUpdate=" + lastUpdate +
                ", processInstances=" + processInstances +
                ", userTasks=" + userTasks +
                '}';
    }
}
