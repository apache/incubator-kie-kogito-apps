/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.taskassigning.core.model.solver.realtime;

import org.kie.kogito.taskassigning.core.model.Task;
import org.kie.kogito.taskassigning.core.model.TaskAssignment;

public class TaskPriorityChangeProblemFactChange extends AbstractTaskPropertyChangeProblemFactChange {

    private String newPriority;

    public TaskPriorityChangeProblemFactChange(TaskAssignment taskAssignment, String newPriority) {
        super(taskAssignment);
        this.newPriority = newPriority;
    }

    @Override
    protected void applyChange(Task task) {
        task.setPriority(newPriority);
    }
}
