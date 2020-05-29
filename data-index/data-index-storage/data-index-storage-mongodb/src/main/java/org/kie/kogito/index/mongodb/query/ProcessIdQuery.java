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

package org.kie.kogito.index.mongodb.query;

import java.util.function.Function;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.mongodb.client.MongoCollection;
import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.kie.kogito.index.mongodb.storage.ProcessIdStorage;
import org.kie.kogito.index.mongodb.model.ProcessIdEntity;
import org.kie.kogito.persistence.mongodb.query.AbstractQuery;

import static java.lang.String.format;

@Dependent
public class ProcessIdQuery extends AbstractQuery<String, ProcessIdEntity> {

    @Inject
    ProcessIdStorage processIdStorage;

    @Override
    protected MongoCollection<ProcessIdEntity> getCollection() {
        return processIdStorage.getCollection();
    }

    @Override
    protected String mapToModel(ProcessIdEntity processIdEntity) {
        return processIdEntity.fullTypeName;
    }

    @Override
    protected Function<String, String> getFilterAttributeFunction() {
        return attribute -> format("'%s'", "processId".equalsIgnoreCase(attribute) ? MongoOperations.ID : attribute);
    }

    @Override
    protected Function<String, String> getSortAttributeFunction() {
        return attribute -> format("%s", "processId".equalsIgnoreCase(attribute) ? MongoOperations.ID : attribute);
    }
}
