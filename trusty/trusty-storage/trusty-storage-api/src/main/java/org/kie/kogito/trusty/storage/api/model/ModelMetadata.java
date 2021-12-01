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

package org.kie.kogito.trusty.storage.api.model;

import java.util.Objects;

import org.kie.kogito.KogitoGAV;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ModelMetadata {

    private static final String IDENTIFIER_TEMPLATE = "%s:%s";
    public static final String GROUP_ID_FIELD = "groupId";
    public static final String ARTIFACT_ID_FIELD = "artifactId";
    public static final String MODEL_VERSION_FIELD = "modelVersion";

    @JsonProperty(GROUP_ID_FIELD)
    private String groupId;

    @JsonProperty(ARTIFACT_ID_FIELD)
    private String artifactId;

    @JsonProperty(MODEL_VERSION_FIELD)
    private String modelVersion;

    public ModelMetadata() {
    }

    public ModelMetadata(String groupId, String artifactId, String modelVersion) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.modelVersion = modelVersion;
    }

    public ModelMetadata(KogitoGAV gav) {
        this(gav.getGroupId(), gav.getArtifactId(), gav.getVersion());
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public abstract String getIdentifier();

    protected String makeIdentifier(final String leftPart,
            final String rightPart) {
        return String.format(IDENTIFIER_TEMPLATE,
                nullable(leftPart),
                nullable(rightPart));
    }

    private String nullable(final String value) {
        return Objects.isNull(value) ? "" : value;
    }

}
