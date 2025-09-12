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
package org.kie.kogito.index.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.kogito.event.usertask.UserTaskInstanceAssignmentDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceAttachmentDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceCommentDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceDeadlineDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceStateDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceVariableDataEvent;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.storage.merger.UserTaskInstanceAssignmentDataEventMerger;
import org.kie.kogito.index.storage.merger.UserTaskInstanceAttachmentDataEventMerger;
import org.kie.kogito.index.storage.merger.UserTaskInstanceCommentDataEventMerger;
import org.kie.kogito.index.storage.merger.UserTaskInstanceDeadlineDataEventMerger;
import org.kie.kogito.index.storage.merger.UserTaskInstanceStateEventMerger;
import org.kie.kogito.index.storage.merger.UserTaskInstanceVariableDataEventMerger;
import org.kie.kogito.persistence.api.Storage;

public class ModelUserTaskInstanceStorage extends ModelStorageFetcher<String, UserTaskInstance> implements UserTaskInstanceStorage {

    private final UserTaskInstanceAssignmentDataEventMerger assignmentMerger = new UserTaskInstanceAssignmentDataEventMerger();
    private final UserTaskInstanceAttachmentDataEventMerger attachmentMerger = new UserTaskInstanceAttachmentDataEventMerger();
    private final UserTaskInstanceCommentDataEventMerger commentMerger = new UserTaskInstanceCommentDataEventMerger();
    private final UserTaskInstanceDeadlineDataEventMerger deadlineMerger = new UserTaskInstanceDeadlineDataEventMerger();
    private final UserTaskInstanceVariableDataEventMerger variableMerger = new UserTaskInstanceVariableDataEventMerger();
    private final UserTaskInstanceStateEventMerger stateMerger = new UserTaskInstanceStateEventMerger();

    public ModelUserTaskInstanceStorage(Storage<String, UserTaskInstance> storage) {
        super(storage);
    }

    @Override
    public void index(List<UserTaskInstanceDataEvent> events) {
        Map<String, UserTaskInstance> userTaskInstances = new HashMap<>();
        for (UserTaskInstanceDataEvent<?> event : events) {
            UserTaskInstance taskInstance = userTaskInstances.computeIfAbsent(event.getKogitoUserTaskInstanceId(), key -> {
                UserTaskInstance ut = storage.get(key);
                if (ut == null) {
                    ut = new UserTaskInstance();
                    ut.setId(key);
                    ut.setProcessInstanceId(event.getKogitoProcessInstanceId());
                    ut.setProcessId(event.getKogitoProcessId());
                    ut.setRootProcessId(event.getKogitoRootProcessId());
                    ut.setRootProcessInstanceId(event.getKogitoRootProcessInstanceId());
                    ut.setAttachments(new ArrayList<>());
                    ut.setComments(new ArrayList<>());
                }
                return ut;
            });
            merge(taskInstance, event);
        }

        for (UserTaskInstance userTaskInstance : userTaskInstances.values()) {
            storage.put(userTaskInstance.getId(), userTaskInstance);
        }
    }

    private void merge(UserTaskInstance taskInstance, UserTaskInstanceDataEvent<?> event) {
        if (event instanceof UserTaskInstanceAssignmentDataEvent) {
            assignmentMerger.merge(taskInstance, event);
        } else if (event instanceof UserTaskInstanceAttachmentDataEvent) {
            attachmentMerger.merge(taskInstance, event);
        } else if (event instanceof UserTaskInstanceCommentDataEvent) {
            commentMerger.merge(taskInstance, event);
        } else if (event instanceof UserTaskInstanceDeadlineDataEvent) {
            deadlineMerger.merge(taskInstance, event);
        } else if (event instanceof UserTaskInstanceVariableDataEvent) {
            variableMerger.merge(taskInstance, event);
        } else if (event instanceof UserTaskInstanceStateDataEvent) {
            stateMerger.merge(taskInstance, event);
        }

    }

}
