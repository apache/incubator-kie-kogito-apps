package org.kie.kogito.explainability.model.dmn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypedData {
    @JsonProperty("name")
    public String inputName;

    @JsonProperty("typeRef")
    public String typeRef;

    @JsonProperty("value")
    public Object value;

    @JsonProperty("components")
    public List<TypedData> components;

    public TypedData(String inputName, String typeRef, List<TypedData> components, Object value) {
        this.inputName = inputName;
        this.typeRef = typeRef;
        this.components = components;
        this.value = value;
    }

    public TypedData(){}
}
