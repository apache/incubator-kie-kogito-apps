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

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntity;

@ApplicationScoped
public class ProcessInstanceQuery extends AbstractEntityQuery<ProcessInstance, ProcessInstanceEntity> {

    @Override
    PanacheQuery<ProcessInstanceEntity> queryWithSort(String queryString, Sort sort) {
        return ProcessInstanceEntity.find(queryString, sort);
    }

    @Override
    PanacheQuery<ProcessInstanceEntity> queryAllWithSort(Sort sort) {
        return ProcessInstanceEntity.findAll(sort);
    }

    @Override
    PanacheQuery<ProcessInstanceEntity> query(String queryString) {
        return ProcessInstanceEntity.find(queryString);
    }

    @Override
    PanacheQuery<ProcessInstanceEntity> queryAll() {
        return ProcessInstanceEntity.findAll();
    }

    @Override
    BiFunction<String, Object, String> getFilterValueAsStringFunction() {
        return (attribute, value) -> "state".equalsIgnoreCase(attribute) ? value.toString()
                : super.getFilterValueAsStringFunction().apply(attribute, value);
    }

    @Override
    Function<ProcessInstanceEntity, ProcessInstance> convertFunction() {
        return ProcessInstanceEntity::toProcessInstance;
    }
}
