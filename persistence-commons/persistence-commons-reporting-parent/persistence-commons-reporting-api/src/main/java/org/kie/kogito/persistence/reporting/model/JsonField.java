package org.kie.kogito.persistence.reporting.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface JsonField<T> {

    String getFieldName();

    T getFieldType();
}
