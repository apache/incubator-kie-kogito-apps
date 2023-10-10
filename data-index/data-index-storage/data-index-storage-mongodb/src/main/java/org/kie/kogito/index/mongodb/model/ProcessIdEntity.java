package org.kie.kogito.index.mongodb.model;

import java.util.Objects;

import org.bson.codecs.pojo.annotations.BsonId;

public class ProcessIdEntity {

    @BsonId
    String processId;

    String fullTypeName;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getFullTypeName() {
        return fullTypeName;
    }

    public void setFullTypeName(String fullTypeName) {
        this.fullTypeName = fullTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessIdEntity that = (ProcessIdEntity) o;
        return Objects.equals(processId, that.processId) &&
                Objects.equals(fullTypeName, that.fullTypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, fullTypeName);
    }
}
