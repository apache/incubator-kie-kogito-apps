/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.explainability.local.lime.optim;

import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;

public class LimeOptimizationResponse {

    private final LimeExplainer explainer;
    private final LimeConfig limeConfig;

    public LimeOptimizationResponse(LimeExplainer explainer, LimeConfig limeConfig) {
        this.explainer = explainer;
        this.limeConfig = limeConfig;
    }

    public LimeConfig getLimeConfig() {
        return limeConfig;
    }

    public LimeExplainer getExplainer() {
        return explainer;
    }
}
