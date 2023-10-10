package org.kie.kogito.persistence.reporting.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseField implements Field {

    public static final String FIELD_NAME_FIELD = "fieldName";

    @JsonProperty(FIELD_NAME_FIELD)
    String fieldName;

    protected BaseField() {
    }

    protected BaseField(final String fieldName) {
        this.fieldName = Objects.requireNonNull(fieldName);
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseField mapping = (BaseField) o;
        return fieldName.equals(mapping.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName);
    }
}
