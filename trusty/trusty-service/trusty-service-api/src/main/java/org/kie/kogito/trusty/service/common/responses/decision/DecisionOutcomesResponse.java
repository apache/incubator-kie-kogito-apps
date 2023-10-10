package org.kie.kogito.trusty.service.common.responses.decision;

import java.util.Collection;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.service.common.responses.OutcomesResponse;
import org.kie.kogito.trusty.storage.api.model.decision.DecisionOutcome;

/**
 * The <b>Decision</b> implementation of <code>OutcomesResponse</code>.
 */
public final class DecisionOutcomesResponse extends OutcomesResponse<DecisionHeaderResponse, DecisionOutcome> {

    protected DecisionOutcomesResponse() {
        // serialization
    }

    public DecisionOutcomesResponse(DecisionHeaderResponse header, Collection<DecisionOutcome> outcomes) {
        super(header, outcomes, ModelDomain.DECISION);
    }
}
