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
package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class FormInfo {

    public enum FormType {
        HTML("html"),
        TSX("tsx");

        private String value;

        private FormType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private final String name;
    private final FormType type;
    private final LocalDateTime lastModified;

    public FormInfo(String name, FormType type, LocalDateTime lastModified) {
        this.name = name;
        this.lastModified = lastModified;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FormType getType() {
        return type;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FormInfo that = (FormInfo) o;

        if (!Objects.equals(name, that.name)) {
            return false;
        }
        if (type != that.type) {
            return false;
        }
        return Objects.equals(lastModified, that.lastModified);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        return result;
    }
}
