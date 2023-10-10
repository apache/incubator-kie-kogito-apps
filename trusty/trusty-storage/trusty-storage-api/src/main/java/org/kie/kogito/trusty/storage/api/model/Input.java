package org.kie.kogito.trusty.storage.api.model;

import org.kie.kogito.tracing.typedvalue.TypedValue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Base abstract class for <b>Input</b>
 * 
 * @param <T>
 * @param <E>
 */
public abstract class Input {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String VALUE_FIELD = "value";

    @JsonProperty(ID_FIELD)
    private String id;

    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonProperty(VALUE_FIELD)
    private TypedValue value;

    protected Input() {
    }

    protected Input(String id, String name, TypedValue value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypedValue getValue() {
        return value;
    }

    public void setValue(TypedValue value) {
        this.value = value;
    }
}
