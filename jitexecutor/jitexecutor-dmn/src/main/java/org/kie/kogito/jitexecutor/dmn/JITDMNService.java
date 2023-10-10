package org.kie.kogito.jitexecutor.dmn;

import java.util.Map;

import org.kie.kogito.jitexecutor.common.requests.MultipleResourcesPayload;
import org.kie.kogito.jitexecutor.dmn.responses.DMNResultWithExplanation;
import org.kie.kogito.jitexecutor.dmn.responses.JITDMNResult;

public interface JITDMNService {

    JITDMNResult evaluateModel(String modelXML, Map<String, Object> context);

    JITDMNResult evaluateModel(MultipleResourcesPayload payload, Map<String, Object> context);

    DMNResultWithExplanation evaluateModelAndExplain(String modelXML, Map<String, Object> context);

    DMNResultWithExplanation evaluateModelAndExplain(MultipleResourcesPayload payload, Map<String, Object> context);
}
