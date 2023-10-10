package org.kie.kogito.persistence.reporting.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseMapping<T, J extends JsonField<T>> implements Mapping<T, J> {

    public static final String SOURCE_JSON_PATH_FIELD = "sourceJsonPath";
    public static final String TARGET_FIELD_FIELD = "targetField";

    @JsonProperty(SOURCE_JSON_PATH_FIELD)
    private String sourceJsonPath;

    @JsonProperty(TARGET_FIELD_FIELD)
    private J targetField;

    protected BaseMapping() {
    }

    protected BaseMapping(final String sourceJsonPath,
            final J targetField) {
        this.sourceJsonPath = Objects.requireNonNull(sourceJsonPath);
        this.targetField = Objects.requireNonNull(targetField);
    }

    @Override
    public String getSourceJsonPath() {
        return sourceJsonPath;
    }

    @Override
    public J getTargetField() {
        return targetField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseMapping)) {
            return false;
        }
        BaseMapping<?, ?> that = (BaseMapping<?, ?>) o;
        return getSourceJsonPath().equals(that.getSourceJsonPath())
                && getTargetField().equals(that.getTargetField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSourceJsonPath(),
                getTargetField());
    }
}
