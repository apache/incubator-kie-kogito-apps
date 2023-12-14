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
package org.kie.kogito.index.oracle.storage;

import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.oracle.mapper.ProcessInstanceEntityMapper;
import org.kie.kogito.index.oracle.model.AbstractEntity;
import org.kie.kogito.index.oracle.model.ProcessInstanceEntity;
import org.kie.kogito.index.oracle.model.ProcessInstanceEntityRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProcessInstanceEntityStorage extends AbstractStorage<ProcessInstanceEntity, ProcessInstance> {

    public ProcessInstanceEntityStorage() {
    }

    @Inject
    public ProcessInstanceEntityStorage(ProcessInstanceEntityRepository repository, ProcessInstanceEntityMapper mapper) {
        super(repository, ProcessInstance.class, ProcessInstanceEntity.class, mapper::mapToModel, mapper::mapToEntity, AbstractEntity::getId);
    }
}
