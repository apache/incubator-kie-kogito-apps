package org.kie.kogito.trusty.service.common.responses.decision;

import java.util.Collection;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.service.common.responses.StructuredInputsResponse;
import org.kie.kogito.trusty.storage.api.model.decision.DecisionInput;

/**
 * The <b>Decision</b> implementation of <code>StructuredInputsResponse</code>.
 */
public class DecisionStructuredInputsResponse extends StructuredInputsResponse<DecisionInput> {

    protected DecisionStructuredInputsResponse() {
        // serialization
    }

    public DecisionStructuredInputsResponse(Collection<DecisionInput> inputs) {
        super(inputs, ModelDomain.DECISION);
    }
}
