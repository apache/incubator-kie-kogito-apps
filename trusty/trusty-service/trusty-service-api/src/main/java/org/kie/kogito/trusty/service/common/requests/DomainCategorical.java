/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.trusty.service.common.requests;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class DomainCategorical implements Domain {

    @JsonProperty("categories")
    private Set<JsonNode> categories;

    public DomainCategorical() {
    }

    public DomainCategorical(Set<JsonNode> categories) {
        this.categories = categories;
    }

    public Set<JsonNode> getCategories() {
        return this.categories;
    }

    @Override
    public String toString() {
        return "DomainCategorical{" +
                "categories=" + categories +
                "}";
    }
}
