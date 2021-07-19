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
package org.kie.kogito.explainability.api;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelIdentifierDto {

    public static final String RESOURCE_ID_SEPARATOR = ":";

    @JsonProperty("resourceType")
    @NotBlank(message = "resourceType must be not blank.")
    private String resourceType;

    @JsonProperty("resourceId")
    @NotBlank(message = "resourceId must be not blank.")
    private String resourceId;

    public ModelIdentifierDto() {
    }

    public ModelIdentifierDto(@NotBlank String resourceType,
            @NotBlank String resourceId) {
        this.resourceType = Objects.requireNonNull(resourceType);
        this.resourceId = Objects.requireNonNull(resourceId);
    }

    @JsonIgnore
    public String getResourceType() {
        return resourceType;
    }

    @JsonIgnore
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @JsonIgnore
    public String getResourceId() {
        return resourceId;
    }

    @JsonIgnore
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
