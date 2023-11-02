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

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TaskInstanceDeadlineLog")
@SequenceGenerator(name = "taskInstanceDeadlineLogIdSeq", sequenceName = "TASK_INSTANCE_DEADLINE_LOG_ID_SEQ")
public class UserTaskInstanceDeadlineLog extends AbstractUserTaskInstanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "taskInstanceDeadlineLogIdSeq")
    private Long id;

    @ElementCollection
    @JoinTable(name = "TaskInstanceDeadlineNotificationLog", joinColumns = @JoinColumn(name = "task_instance_deadline_log_id"), foreignKey = @ForeignKey(name = "fk_task_instance_deadline_tid"))
    @MapKeyColumn(name = "property_name")
    @Column(name = "property_value")
    private Map<String, String> notification;

    @Column(name = "event_type")
    private String eventType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getNotification() {
        return notification;
    }

    public void setNotification(Map<String, String> notification) {
        this.notification = notification;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

}
