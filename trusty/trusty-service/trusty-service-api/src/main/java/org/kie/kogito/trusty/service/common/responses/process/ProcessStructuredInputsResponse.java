package org.kie.kogito.trusty.service.common.responses.process;

import java.util.Collection;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.service.common.responses.StructuredInputsResponse;
import org.kie.kogito.trusty.storage.api.model.process.ProcessInput;

/**
 * The <b>Process</b> implementation of <code>StructuredInputsResponse</code>.
 */
public class ProcessStructuredInputsResponse extends StructuredInputsResponse<ProcessInput> {

    protected ProcessStructuredInputsResponse() {
        // serialization
    }

    public ProcessStructuredInputsResponse(Collection<ProcessInput> inputs) {
        super(inputs, ModelDomain.PROCESS);
    }
}
