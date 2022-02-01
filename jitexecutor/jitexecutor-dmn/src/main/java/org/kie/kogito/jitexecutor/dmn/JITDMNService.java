/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.jitexecutor.dmn;

import java.util.Map;

import org.kie.kogito.jitexecutor.dmn.requests.MultipleResourcesPayload;
import org.kie.kogito.jitexecutor.dmn.responses.DMNResultWithExplanation;
import org.kie.kogito.jitexecutor.dmn.responses.JITDMNResult;

public interface JITDMNService {

    JITDMNResult evaluateModel(String modelXML, Map<String, Object> context);

    JITDMNResult evaluateModel(MultipleResourcesPayload payload, Map<String, Object> context);

    DMNResultWithExplanation evaluateModelAndExplain(String modelXML, Map<String, Object> context);

    DMNResultWithExplanation evaluateModelAndExplain(MultipleResourcesPayload payload, Map<String, Object> context);
}
