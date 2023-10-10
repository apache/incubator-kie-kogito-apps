package org.kie.kogito.persistence.reporting.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BasePartitionField extends BaseField implements PartitionField {

    public static final String FIELD_VALUE_FIELD = "fieldValue";

    @JsonProperty(FIELD_VALUE_FIELD)
    String fieldValue;

    protected BasePartitionField() {
    }

    protected BasePartitionField(final String fieldName,
            final String fieldValue) {
        super(fieldName);
        this.fieldValue = Objects.requireNonNull(fieldValue);
    }

    @Override
    public String getFieldValue() {
        return fieldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BasePartitionField)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BasePartitionField that = (BasePartitionField) o;
        return getFieldValue().equals(that.getFieldValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFieldValue());
    }
}
