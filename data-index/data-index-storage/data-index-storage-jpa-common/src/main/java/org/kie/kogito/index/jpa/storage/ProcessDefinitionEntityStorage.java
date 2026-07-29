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
package org.kie.kogito.index.jpa.storage;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.hibernate.exception.ConstraintViolationException;
import org.kie.kogito.index.jpa.mapper.ProcessDefinitionEntityMapper;
import org.kie.kogito.index.jpa.model.ProcessDefinitionEntity;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.model.ProcessDefinitionKey;
import org.kie.kogito.process.Processes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

import jakarta.persistence.EntityManager;

import static org.kie.kogito.index.DependencyInjectionUtils.getInstance;

public class ProcessDefinitionEntityStorage extends AbstractStorage<ProcessDefinitionKey, ProcessDefinitionEntity, ProcessDefinition> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDefinitionEntityStorage.class);

    protected ProcessDefinitionEntityStorage() {
    }

    public ProcessDefinitionEntityStorage(EntityManager em, Iterable<JsonPredicateBuilder> predicateBuilder, Iterable<Processes> processes) {
        this(em, predicateBuilder, ProcessDefinitionEntityMapper.INSTANCE, processes);
    }

    public ProcessDefinitionEntityStorage(EntityManager em, Iterable<JsonPredicateBuilder> predicateBuilder, ProcessDefinitionEntityMapper mapper, Iterable<Processes> processes) {
        super(em, ProcessDefinition.class, ProcessDefinitionEntity.class, mapper::mapToModel, mapper.INSTANCE::mapToEntity, e -> new ProcessDefinitionKey(e.getId(),
                e.getVersion()), Optional.ofNullable(getInstance(predicateBuilder)), Optional.ofNullable(getInstance(processes)));
    }

    protected ProcessDefinition put(ProcessDefinitionKey key, ProcessDefinition value,
            Function<Supplier<ProcessDefinition>, ProcessDefinition> isolatedTransaction) {
        try {
            return isolatedTransaction.apply(() -> {
                super.put(key, value);
                em.flush();
                return value;
            });
        } catch (RuntimeException e) {
            if (Throwables.getCausalChain(e).stream().noneMatch(ConstraintViolationException.class::isInstance)) {
                throw e;
            }
            LOGGER.info("ProcessDefinition with id '{}' and version '{}' is already present, skipping insert.", key.getId(), key.getVersion());
            LOGGER.debug("Duplicate ProcessDefinition insert suppressed", e);
            return get(key);
        }
    }

}
