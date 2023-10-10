package org.kie.kogito.index.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeInstance {

    private String id;
    private String nodeId;

    @JsonProperty("nodeName")
    private String name;

    @JsonProperty("nodeType")
    private String type;

    @JsonProperty("triggerTime")
    private ZonedDateTime enter;
    @JsonProperty("leaveTime")
    private ZonedDateTime exit;

    @JsonProperty("nodeDefinitionId")
    private String definitionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getEnter() {
        return enter;
    }

    public void setEnter(ZonedDateTime enter) {
        this.enter = enter;
    }

    public ZonedDateTime getExit() {
        return exit;
    }

    public void setExit(ZonedDateTime exit) {
        this.exit = exit;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NodeInstance)) {
            return false;
        }

        NodeInstance that = (NodeInstance) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "NodeInstance{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", type='" + type + '\'' +
                ", enter=" + enter +
                ", exit=" + exit +
                ", definitionId='" + definitionId + '\'' +
                '}';
    }
}
