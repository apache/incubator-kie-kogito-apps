package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class FormInfo {

    public enum FormType {
        HTML("html"),
        TSX("tsx");

        private final String value;

        FormType(String value) {
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

        if (!Objects.equals(getName(), that.getName())) {
            return false;
        }
        if (getType() != that.getType()) {
            return false;
        }
        return Objects.equals(getLastModified(), that.getLastModified());
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getLastModified() != null ? getLastModified().hashCode() : 0);
        return result;
    }
}
