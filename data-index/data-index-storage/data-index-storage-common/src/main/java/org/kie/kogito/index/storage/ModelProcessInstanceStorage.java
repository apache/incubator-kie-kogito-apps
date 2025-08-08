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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.process.ProcessInstanceErrorDataEvent;
import org.kie.kogito.event.process.ProcessInstanceNodeDataEvent;
import org.kie.kogito.event.process.ProcessInstanceSLADataEvent;
import org.kie.kogito.event.process.ProcessInstanceStateDataEvent;
import org.kie.kogito.event.process.ProcessInstanceVariableDataEvent;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.storage.merger.ProcessInstanceErrorDataEventMerger;
import org.kie.kogito.index.storage.merger.ProcessInstanceNodeDataEventMerger;
import org.kie.kogito.index.storage.merger.ProcessInstanceSLADataEventMerger;
import org.kie.kogito.index.storage.merger.ProcessInstanceStateDataEventMerger;
import org.kie.kogito.index.storage.merger.ProcessInstanceVariableDataEventMerger;
import org.kie.kogito.persistence.api.Storage;

public class ModelProcessInstanceStorage extends ModelStorageFetcher<String, ProcessInstance> implements ProcessInstanceStorage {
    private final ProcessInstanceErrorDataEventMerger errorMerger = new ProcessInstanceErrorDataEventMerger();
    private final ProcessInstanceNodeDataEventMerger nodeMerger = new ProcessInstanceNodeDataEventMerger();
    private final ProcessInstanceSLADataEventMerger slaMerger = new ProcessInstanceSLADataEventMerger();
    private final ProcessInstanceStateDataEventMerger stateMerger = new ProcessInstanceStateDataEventMerger();
    private final ProcessInstanceVariableDataEventMerger variableMerger = new ProcessInstanceVariableDataEventMerger();

    public ModelProcessInstanceStorage(Storage<String, ProcessInstance> storage) {
        super(storage);
    }

    @Override
    public void index(List<ProcessInstanceDataEvent> events) {
        Map<String, ProcessInstance> processInstances = new HashMap<>();
        for (ProcessInstanceDataEvent<?> event : events) {
            ProcessInstance processInstance = processInstances.computeIfAbsent(event.getKogitoProcessInstanceId(), key -> {
                ProcessInstance pi = storage.get(key);
                if (pi == null) {
                    pi = new ProcessInstance();
                    pi.setId(event.getKogitoProcessInstanceId());
                    pi.setProcessId(event.getKogitoProcessId());
                }
                return pi;
            });
            merge(processInstance, event);
        }

        for (ProcessInstance processInstance : processInstances.values()) {
            storage.put(processInstance.getId(), processInstance);
        }
    }

    private void merge(ProcessInstance pi, ProcessInstanceDataEvent<?> event) {
        if (event instanceof ProcessInstanceErrorDataEvent) {
            errorMerger.merge(pi, event);
        } else if (event instanceof ProcessInstanceNodeDataEvent) {
            nodeMerger.merge(pi, event);
        } else if (event instanceof ProcessInstanceSLADataEvent) {
            slaMerger.merge(pi, event);
        } else if (event instanceof ProcessInstanceStateDataEvent) {
            stateMerger.merge(pi, event);
        } else if (event instanceof ProcessInstanceVariableDataEvent) {
            variableMerger.merge(pi, event);
        }
    }

}
