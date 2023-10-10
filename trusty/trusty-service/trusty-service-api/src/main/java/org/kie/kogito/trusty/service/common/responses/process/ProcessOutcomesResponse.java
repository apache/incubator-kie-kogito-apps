package org.kie.kogito.trusty.service.common.responses.process;

import java.util.Collection;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.service.common.responses.OutcomesResponse;
import org.kie.kogito.trusty.storage.api.model.process.ProcessOutcome;

/**
 * The <b>Process</b> implementation of <code>OutcomesResponse</code>.
 */
public final class ProcessOutcomesResponse extends OutcomesResponse<ProcessHeaderResponse, ProcessOutcome> {

    protected ProcessOutcomesResponse() {
        // serialization
    }

    public ProcessOutcomesResponse(ProcessHeaderResponse header, Collection<ProcessOutcome> outcomes) {
        super(header, outcomes, ModelDomain.PROCESS);
    }
}
