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

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.process.ProcessInstanceErrorDataEvent;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.ProcessInstanceError;

@ApplicationScoped
public class ProcessInstanceErrorDataEventMerger implements ProcessInstanceEventMerger {

    @Override
    public boolean accept(ProcessInstanceDataEvent<?> event) {
        return event instanceof ProcessInstanceErrorDataEvent;
    }

    @Override
    public void merge(ProcessInstance pi, ProcessInstanceDataEvent<?> data) {
        ProcessInstanceErrorDataEvent event = (ProcessInstanceErrorDataEvent) data;
        ProcessInstanceError error = new ProcessInstanceError();
        error.setMessage(event.getData().getErrorMessage());
        error.setNodeDefinitionId(event.getData().getNodeDefinitionId());
        pi.setError(error);
    }

}
