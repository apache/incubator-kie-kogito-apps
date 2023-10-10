package org.kie.kogito.trusty.storage.api.model.process;

import org.kie.kogito.tracing.typedvalue.TypedValue;
import org.kie.kogito.trusty.storage.api.model.Input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessInput extends Input {

    public ProcessInput() {
    }

    public ProcessInput(String id, String name, TypedValue value) {
        super(id, name, value);
    }
}
