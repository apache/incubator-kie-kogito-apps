package org.kie.kogito.index.postgresql.model;

import java.io.Serializable;
import java.util.Objects;

public class ProcessDefinitionEntityId implements Serializable {

    private String id;

    private String version;

    public ProcessDefinitionEntityId() {
    }

    public ProcessDefinitionEntityId(String key) {
        String[] split = key.split("-");
        this.id = split[0];
        this.version = split[1];
    }

    public ProcessDefinitionEntityId(String id, String version) {
        this.id = id;
        this.version = version;
    }

    public String getKey() {
        return String.format("%s-%s", id, version);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProcessDefinitionEntityId that = (ProcessDefinitionEntityId) o;
        return Objects.equals(id, that.id) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    @Override
    public String toString() {
        return "ProcessDefinitionEntityId{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
