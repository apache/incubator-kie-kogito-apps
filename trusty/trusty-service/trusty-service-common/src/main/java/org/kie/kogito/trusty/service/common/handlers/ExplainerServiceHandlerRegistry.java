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
package org.kie.kogito.trusty.service.common.handlers;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.kie.kogito.explainability.api.BaseExplainabilityResultDto;
import org.kie.kogito.trusty.storage.api.model.BaseExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.Decision;

@ApplicationScoped
public class ExplainerServiceHandlerRegistry {

    private Instance<ExplainerServiceHandler<?, ?>> explanationHandlers;

    protected ExplainerServiceHandlerRegistry() {
        //CDI proxy
    }

    @Inject
    public ExplainerServiceHandlerRegistry(@Any Instance<ExplainerServiceHandler<?, ?>> explanationHandlers) {
        this.explanationHandlers = explanationHandlers;
    }

    /**
     * Gets the result for an explanation.
     *
     * @param executionId The execution Id for which to retrieve an explanation result.
     * @param type The type of result to retrieve.
     * @return The result of an explanation.
     */
    public <T extends BaseExplainabilityResult> T getExplainabilityResultById(String executionId, Class<T> type) {
        ExplainerServiceHandler<?, ?> explanationHandler = getLocalExplainer(type)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Explainability result for '%s' is not supported", type.getName())));

        return cast(explanationHandler.getExplainabilityResultById(executionId));
    }

    /**
     * Stores the result for an explanation.
     *
     * @param executionId The execution Id for which to retrieve an explanation result.
     * @param result The result to store.
     */
    public <T extends BaseExplainabilityResult> void storeExplainabilityResult(String executionId, T result) {
        T type = cast(result);
        ExplainerServiceHandler<?, ?> explanationHandler =
                getLocalExplainer(type.getClass())
                        .orElseThrow(() -> new IllegalArgumentException(String.format("Explainability result for '%s' is not supported", type.getClass().getName())));

        explanationHandler.storeExplainabilityResult(executionId, cast(result));
    }

    /**
     * Converts the result from the Explainability Service to that used by Trusty Service.
     *
     * @param dto The result from the Explainability Service
     * @param decision The decision for which the explaination was requested
     * @return The result used by Trusty Service
     */
    public <T extends BaseExplainabilityResultDto> BaseExplainabilityResult explainabilityResultFrom(T dto, Decision decision) {
        if (dto == null) {
            return null;
        }

        ExplainerServiceHandler<?, ?> explanationHandler =
                getLocalExplainerDto(dto.getClass()).orElseThrow(() -> new IllegalArgumentException(String.format("Explainability result for '%s' is not supported", dto.getClass().getName())));

        return explanationHandler.explainabilityResultFrom(castDto(dto), decision);
    }

    private <T extends BaseExplainabilityResult> Optional<ExplainerServiceHandler<?, ?>> getLocalExplainer(Class<T> type) {
        return this.explanationHandlers.stream().filter(handler -> handler.supports(type)).findFirst();
    }

    private <T extends BaseExplainabilityResultDto> Optional<ExplainerServiceHandler<?, ?>> getLocalExplainerDto(Class<T> type) {
        return this.explanationHandlers.stream().filter(handler -> handler.supportsDto(type)).findFirst();
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseExplainabilityResult> T cast(BaseExplainabilityResult type) {
        return (T) type;
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseExplainabilityResultDto> T castDto(BaseExplainabilityResultDto type) {
        return (T) type;
    }

}
