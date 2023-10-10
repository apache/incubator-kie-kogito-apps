package org.kie.kogito.persistence.reporting.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseJsonField<T> implements JsonField<T> {

    public static final String FIELD_NAME_FIELD = "fieldName";
    public static final String FIELD_TYPE_FIELD = "fieldType";

    @JsonProperty(FIELD_NAME_FIELD)
    String fieldName;

    @JsonProperty(FIELD_TYPE_FIELD)
    T fieldType;

    protected BaseJsonField() {
    }

    protected BaseJsonField(final String fieldName,
            final T fieldType) {
        this.fieldName = Objects.requireNonNull(fieldName);
        this.fieldType = Objects.requireNonNull(fieldType);
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public T getFieldType() {
        return fieldType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseJsonField<?> mapping = (BaseJsonField<?>) o;
        return fieldName.equals(mapping.fieldName)
                && fieldType.equals(mapping.fieldType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, fieldType);
    }
}
