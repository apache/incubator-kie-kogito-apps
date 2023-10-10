package org.kie.kogito.trusty.storage.api.model.decision;

import org.kie.kogito.tracing.typedvalue.TypedValue;
import org.kie.kogito.trusty.storage.api.model.Input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The <b>Decision</b> implementation of <code>Input</code>.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecisionInput extends Input {

    public DecisionInput() {
    }

    public DecisionInput(String id, String name, TypedValue value) {
        super(id, name, value);
    }
}
