/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.index.event.mapper;

import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.index.model.ProcessInstance;

public interface ProcessInstanceEventMerger {

    boolean accept(ProcessInstanceDataEvent<?> event);

    void merge(ProcessInstance pi, ProcessInstanceDataEvent<?> event);

}
