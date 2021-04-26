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
package org.kie.kogito.trusty.storage.api.model;

import java.util.Collection;

import org.kie.kogito.tracing.typedvalue.TypedValue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterfactualSearchDomain extends TypedVariable<CounterfactualSearchDomain> {

    public static final String IS_FIXED = "isFixed";
    public static final String DOMAIN = "domain";

    @JsonProperty(IS_FIXED)
    private Boolean isFixed;

    @JsonProperty(DOMAIN)
    private CounterfactualDomain domain;

    public static CounterfactualSearchDomain buildCollection(String name, String typeRef, Collection<CounterfactualSearchDomain> components,
            Boolean isFixed,
            CounterfactualDomain domain) {
        return new CounterfactualSearchDomain(TypedValue.Kind.COLLECTION, name, typeRef, components, isFixed, domain);
    }

    public static CounterfactualSearchDomain buildStructure(String name, String typeRef, Collection<CounterfactualSearchDomain> components,
            Boolean isFixed,
            CounterfactualDomain domain) {
        return new CounterfactualSearchDomain(TypedValue.Kind.STRUCTURE, name, typeRef, components, isFixed, domain);
    }

    public static CounterfactualSearchDomain buildUnit(String name, String typeRef,
            Boolean isFixed,
            CounterfactualDomain domain) {
        return new CounterfactualSearchDomain(TypedValue.Kind.UNIT, name, typeRef, null, isFixed, domain);
    }

    public CounterfactualSearchDomain() {
    }

    public CounterfactualSearchDomain(TypedValue.Kind kind,
            String name,
            String typeRef,
            Collection<CounterfactualSearchDomain> components,
            Boolean isFixed,
            CounterfactualDomain domain) {
        super(kind, name, typeRef, components);
        this.isFixed = isFixed;
        this.domain = domain;
    }

    public Boolean isFixed() {
        return isFixed;
    }

    public CounterfactualDomain getDomain() {
        return domain;
    }

    //-------------
    // Test methods
    //-------------

    public void setFixed(Boolean isFixed) {
        this.isFixed = isFixed;
    }

    public void setDomain(CounterfactualDomain domain) {
        this.domain = domain;
    }
}
