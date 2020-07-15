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
    public String id;

    public String processId;

    public Set<String> roles;

    public Document variables;

    public String endpoint;

    public List<NodeInstanceEntity> nodes;

    public Integer state;

    public Long start;

    public Long end;

    public String rootProcessInstanceId;

    public String rootProcessId;

    public String parentProcessInstanceId;

    public String processName;

    public ProcessInstanceErrorEntity error;

    public Set<String> addons;

    public Long lastUpdate;

    public String businessKey;

    public List<MilestoneEntity> milestones;

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

        public String id;

        public String name;

        public String nodeId;

        public String type;

        public Long enter;

        public Long exit;

        public String definitionId;

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

        public String nodeDefinitionId;

        public String message;

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

        public String id;

        public String name;

        public String status;

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
