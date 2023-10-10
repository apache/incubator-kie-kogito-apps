package org.kie.kogito.trusty.storage.api.model.decision;

import java.util.Collection;

import org.kie.kogito.explainability.api.NamedTypedValue;
import org.kie.kogito.tracing.event.message.Message;
import org.kie.kogito.tracing.typedvalue.TypedValue;
import org.kie.kogito.trusty.storage.api.model.Outcome;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The <b>Decision</b> implementation of <code>Outcome</code>.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecisionOutcome extends Outcome {

    public DecisionOutcome() {
    }

    public DecisionOutcome(String outcomeId, String outcomeName, String evaluationStatus,
            TypedValue outcomeResult,
            Collection<NamedTypedValue> outcomeInputs,
            Collection<Message> messages) {
        super(outcomeId, outcomeName, evaluationStatus,
                outcomeResult,
                outcomeInputs,
                messages);
    }

}
