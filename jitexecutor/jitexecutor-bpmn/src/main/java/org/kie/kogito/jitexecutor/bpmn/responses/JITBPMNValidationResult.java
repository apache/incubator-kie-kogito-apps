package org.kie.kogito.jitexecutor.bpmn.responses;

import java.io.Serializable;
import java.util.Collection;

public class JITBPMNValidationResult implements Serializable {

    private final Collection<String> errors;

    public JITBPMNValidationResult(Collection<String> errors) {
        this.errors = errors;
    }

    public Collection<String> getErrors() {
        return errors;
    }
}
