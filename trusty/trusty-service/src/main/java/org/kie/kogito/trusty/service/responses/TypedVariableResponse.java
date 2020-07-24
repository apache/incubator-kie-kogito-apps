/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kie.kogito.trusty.service.responses;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.kie.kogito.trusty.storage.api.model.TypedVariable;

public class TypedVariableResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("typeRef")
    private String typeRef;

    @JsonProperty("value")
    private JsonNode value;

    @JsonProperty("components")
    private List<TypedVariableResponse> components;

    private TypedVariableResponse() {
    }

    public TypedVariableResponse(String name, String typeRef, JsonNode value, List<TypedVariableResponse> components) {
        this.name = name;
        this.typeRef = typeRef;
        this.value = value;
        this.components = components;
    }

    public String getName() {
        return name;
    }

    public String getTypeRef() {
        return typeRef;
    }

    public JsonNode getValue() {
        return value;
    }

    public List<TypedVariableResponse> getComponents() {
        return components;
    }

    public static TypedVariableResponse from(TypedVariable value) {
        if (value == null) {
            return null;
        }

        List<TypedVariableResponse> components = value.getComponents() == null
                ? null
                : value.getComponents().stream().map(TypedVariableResponse::from).collect(Collectors.toList());

        return new TypedVariableResponse(value.getName(), value.getTypeRef(), value.getValue(), components);
    }
}
