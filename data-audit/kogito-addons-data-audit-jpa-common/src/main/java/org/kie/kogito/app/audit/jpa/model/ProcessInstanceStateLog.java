/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.kie.kogito.app.audit.jpa.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ProcessInstanceStateLog")
@SequenceGenerator(name = "processInstanceStateLogIdSeq", sequenceName = "PROCESS_INSTANCE_STATE_LOG_ID_SEQ")
public class ProcessInstanceStateLog extends AbstractProcessInstanceLog {

    public enum ProcessStateLogType {
        ACTIVE,
        STARTED,
        COMPLETED,
        ABORTED,
        SLA_VIOLATION,
        PENDING,
        SUSPENDING,
        ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "processInstanceStateLogIdSeq")
    private long id;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProcessStateLogType eventType;

    @Column
    private String outcome;

    @Column
    private String state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sla_due_date")
    private Date slaDueDate;

    @ElementCollection
    @CollectionTable(name = "ProcessInstanceStateRolesLog", joinColumns = @JoinColumn(name = "process_instance_id", foreignKey = @ForeignKey(name = "fk_process_instance_state_pid")))
    @Column(name = "role")
    private Set<String> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProcessStateLogType getEventType() {
        return eventType;
    }

    public void setEventType(ProcessStateLogType eventType) {
        this.eventType = eventType;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Date getSlaDueDate() {
        return slaDueDate;
    }

    public void setSlaDueDate(Date slaDueDate) {
        this.slaDueDate = slaDueDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getRoles() {
        return roles;
    }

}