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
package org.kie.kogito.trusty.storage.api.model.decision;

import org.kie.kogito.trusty.storage.api.model.ModelMetadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DMNModelMetadata extends ModelMetadata {

    public static final String DMN_VERSION_FIELD = "dmnVersion";
    public static final String NAME_FIELD = "name";
    public static final String NAMESPACE_FIELD = "namespace";

    @JsonProperty(DMN_VERSION_FIELD)
    private String dmnVersion;

    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonProperty(NAMESPACE_FIELD)
    private String namespace;

    private String identifier = null;

    public DMNModelMetadata() {
    }

    public DMNModelMetadata(String groupId, String artifactId, String modelVersion, String dmnVersion,
            String name, String namespace) {
        super(groupId, artifactId, modelVersion);
        this.dmnVersion = dmnVersion;
        this.name = name;
        this.namespace = namespace;
        this.identifier = makeIdentifier(name, namespace);
    }

    public String getDmnVersion() {
        return dmnVersion;
    }

    public void setDmnVersion(String dmnVersion) {
        this.dmnVersion = dmnVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }
}
