package org.kie.kogito.jitexecutor.bpmn;

import org.kie.kogito.jitexecutor.bpmn.responses.JITBPMNValidationResult;
import org.kie.kogito.jitexecutor.common.requests.MultipleResourcesPayload;

public interface JITBPMNService {

    JITBPMNValidationResult validateModel(String modelXml);

    JITBPMNValidationResult validatePayload(MultipleResourcesPayload payload);
}
