/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.index.mongodb.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;

public class ProcessInstanceEntity {

    @BsonId
    String id;

    String processId;

    Set<String> roles;

    Document variables;

    String endpoint;

    List<NodeInstanceEntity> nodes;

    Integer state;

    Long start;

    Long end;

    String rootProcessInstanceId;

    String rootProcessId;

    String parentProcessInstanceId;

    String processName;

    ProcessInstanceErrorEntity error;

    Set<String> addons;

    Long lastUpdate;

    String businessKey;

    List<MilestoneEntity> milestones;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Document getVariables() {
        return variables;
    }

    public void setVariables(Document variables) {
        this.variables = variables;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public List<NodeInstanceEntity> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeInstanceEntity> nodes) {
        this.nodes = nodes;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public String getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }

    public void setRootProcessInstanceId(String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    public String getRootProcessId() {
        return rootProcessId;
    }

    public void setRootProcessId(String rootProcessId) {
        this.rootProcessId = rootProcessId;
    }

    public String getParentProcessInstanceId() {
        return parentProcessInstanceId;
    }

    public void setParentProcessInstanceId(String parentProcessInstanceId) {
        this.parentProcessInstanceId = parentProcessInstanceId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public ProcessInstanceErrorEntity getError() {
        return error;
    }

    public void setError(ProcessInstanceErrorEntity error) {
        this.error = error;
    }

    public Set<String> getAddons() {
        return addons;
    }

    public void setAddons(Set<String> addons) {
        this.addons = addons;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public List<MilestoneEntity> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<MilestoneEntity> milestones) {
        this.milestones = milestones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessInstanceEntity that = (ProcessInstanceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class NodeInstanceEntity {

        String id;

        String name;

        String nodeId;

        String type;

        Long enter;

        Long exit;

        String definitionId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Long getEnter() {
            return enter;
        }

        public void setEnter(Long enter) {
            this.enter = enter;
        }

        public Long getExit() {
            return exit;
        }

        public void setExit(Long exit) {
            this.exit = exit;
        }

        public String getDefinitionId() {
            return definitionId;
        }

        public void setDefinitionId(String definitionId) {
            this.definitionId = definitionId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            NodeInstanceEntity that = (NodeInstanceEntity) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    public static class ProcessInstanceErrorEntity {

        String nodeDefinitionId;

        String message;

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

    public static class MilestoneEntity {

        String id;

        String name;

        String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MilestoneEntity that = (MilestoneEntity) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
