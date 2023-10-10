package org.kie.kogito.trusty.storage.api.model.process;

import java.util.Collection;

import org.kie.kogito.explainability.api.NamedTypedValue;
import org.kie.kogito.tracing.event.message.Message;
import org.kie.kogito.tracing.typedvalue.TypedValue;
import org.kie.kogito.trusty.storage.api.model.Outcome;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The <b>Process</b> implementation of <code>Outcome</code>.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessOutcome extends Outcome {

    public ProcessOutcome() {
    }

    public ProcessOutcome(String outcomeId, String outcomeName, String evaluationStatus,
            TypedValue outcomeResult,
            Collection<NamedTypedValue> outcomeInputs,
            Collection<Message> messages) {
        super(outcomeId, outcomeName, evaluationStatus,
                outcomeResult,
                outcomeInputs,
                messages);
    }

}
