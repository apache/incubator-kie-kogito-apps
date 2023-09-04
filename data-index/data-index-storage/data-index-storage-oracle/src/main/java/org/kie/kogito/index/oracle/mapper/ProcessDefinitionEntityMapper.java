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

package org.kie.kogito.index.oracle.mapper;

import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.oracle.model.ProcessDefinitionEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface ProcessDefinitionEntityMapper {

    ProcessDefinitionEntity mapToEntity(ProcessDefinition pd);

    @InheritInverseConfiguration
    ProcessDefinition mapToModel(ProcessDefinitionEntity pd);

    default byte[] map(String value) {
        return value == null ? null : value.getBytes();
    };

    default String map(byte[] value) {
        return value == null ? null : new String(value);
    }
}
