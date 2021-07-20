/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.index.postgresql.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.kie.kogito.persistence.postgresql.hibernate.JsonBinaryType;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ProcessInstanceEntity extends AbstractEntity {

    @Id
    private String id;
    private String processId;
    private String processName;
    private Integer state;
    private String businessKey;
    private String endpoint;
    @ElementCollection
    @JoinColumn(name = "ROLE", referencedColumnName = "ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<String> roles;
    @Column(name = "start_time")
    private ZonedDateTime start;
    @Column(name = "end_time")
    private ZonedDateTime end;
    private String rootProcessInstanceId;
    private String rootProcessId;
    private String parentProcessInstanceId;
    @Column(name = "last_update_time")
    private ZonedDateTime lastUpdate;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private ObjectNode variables;
    @OneToMany(cascade = CascadeType.ALL)
    private List<NodeInstanceEntity> nodes;
    @OneToMany(cascade = CascadeType.ALL)
    private List<MilestoneEntity> milestones;
    @ElementCollection
    @JoinColumn(name = "ADDON", referencedColumnName = "ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<String> addons;
    @Embedded
    private ProcessInstanceErrorEntity error;

    @Override
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

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
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

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setVariables(ObjectNode variables) {
        this.variables = variables;
    }

    public ObjectNode getVariables() {
        return variables;
    }

    public List<NodeInstanceEntity> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeInstanceEntity> nodes) {
        this.nodes = nodes;
    }

    public List<MilestoneEntity> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<MilestoneEntity> milestones) {
        this.milestones = milestones;
    }

    public Set<String> getAddons() {
        return addons;
    }

    public void setAddons(Set<String> addons) {
        this.addons = addons;
    }

    public void setError(ProcessInstanceErrorEntity error) {
        this.error = error;
    }

    public ProcessInstanceErrorEntity getError() {
        return error;
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

    @Override
    public String toString() {
        return "ProcessInstanceEntity{" +
                "id='" + id + '\'' +
                ", processId='" + processId + '\'' +
                ", processName='" + processName + '\'' +
                ", state=" + state +
                ", businessKey='" + businessKey + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", roles=" + roles +
                ", start=" + start +
                ", end=" + end +
                ", rootProcessInstanceId='" + rootProcessInstanceId + '\'' +
                ", rootProcessId='" + rootProcessId + '\'' +
                ", parentProcessInstanceId='" + parentProcessInstanceId + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", variables=" + variables +
                ", nodes=" + nodes +
                ", milestones=" + milestones +
                ", addons=" + addons +
                ", error=" + error +
                '}';
    }
}
