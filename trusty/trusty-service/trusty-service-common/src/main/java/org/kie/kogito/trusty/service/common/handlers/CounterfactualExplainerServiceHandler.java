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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.explainability.api.BaseExplainabilityResultDto;
import org.kie.kogito.explainability.api.CounterfactualExplainabilityResultDto;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.AttributeFilter;
import org.kie.kogito.persistence.api.query.QueryFilterFactory;
import org.kie.kogito.trusty.service.common.messaging.MessagingUtils;
import org.kie.kogito.trusty.storage.api.model.BaseExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.CounterfactualExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.common.TrustyStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CounterfactualExplainerServiceHandler extends BaseExplainerServiceHandler<CounterfactualExplainabilityResult, CounterfactualExplainabilityResultDto> {

    private static final Logger LOG = LoggerFactory.getLogger(CounterfactualExplainerServiceHandler.class);

    private final CounterfactualSlidingWindowExplainabilityResultsManager explainabilityResultsManager;

    //CDI proxy
    protected CounterfactualExplainerServiceHandler() {
        this(null, null);
    }

    @Inject
    public CounterfactualExplainerServiceHandler(TrustyStorageService storageService,
            CounterfactualSlidingWindowExplainabilityResultsManager explainabilityResultsManager) {
        super(storageService);
        this.explainabilityResultsManager = explainabilityResultsManager;
    }

    @Override
    public <T extends BaseExplainabilityResult> boolean supports(Class<T> type) {
        return CounterfactualExplainabilityResult.class.isAssignableFrom(type);
    }

    @Override
    public <T extends BaseExplainabilityResultDto> boolean supportsDto(Class<T> type) {
        return CounterfactualExplainabilityResultDto.class.isAssignableFrom(type);
    }

    @Override
    public CounterfactualExplainabilityResult explainabilityResultFrom(CounterfactualExplainabilityResultDto dto, Decision decision) {
        return new CounterfactualExplainabilityResult(dto.getExecutionId(),
                dto.getCounterfactualId(),
                dto.getSolutionId(),
                dto.getSequenceId(),
                statusFrom(dto.getStatus()),
                dto.getStatusDetails(),
                dto.isValid(),
                stageFrom(dto.getStage()),
                MessagingUtils.tracingTypedValueToModel(dto.getInputs()),
                MessagingUtils.tracingTypedValueToModel(dto.getOutputs()));
    }

    @Override
    public CounterfactualExplainabilityResult getExplainabilityResultById(String executionId) {
        Storage<String, CounterfactualExplainabilityResult> storage = storageService.getCounterfactualResultStorage();

        AttributeFilter<String> filterExecutionId = QueryFilterFactory.equalTo(CounterfactualExplainabilityResult.EXECUTION_ID_FIELD, executionId);
        List<CounterfactualExplainabilityResult> counterfactuals = storage.query().filter(Collections.singletonList(filterExecutionId)).execute();

        if (Objects.isNull(counterfactuals) || counterfactuals.isEmpty()) {
            throw new IllegalArgumentException(String.format("A Counterfactual result for Execution ID '%s' does not exist in the Counterfactual results storage.", executionId));
        }

        return counterfactuals
                .stream()
                .filter(counterfactual -> counterfactual.getStage().equals(CounterfactualExplainabilityResult.Stage.FINAL))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(String.format("A FINAL Counterfactual result for Execution ID '%s' does not exist in the Counterfactual results storage.", executionId)));
    }

    @Override
    public void storeExplainabilityResult(String executionId, CounterfactualExplainabilityResult result) {
        String solutionId = result.getSolutionId();
        String counterfactualId = result.getCounterfactualId();
        Storage<String, CounterfactualExplainabilityResult> storage = storageService.getCounterfactualResultStorage();
        if (storage.containsKey(solutionId)) {
            throw new IllegalArgumentException(
                    String.format("A Counterfactual result for Execution ID '%s' and SolutionId '%s' is already present in the Counterfactual results storage.", executionId, solutionId));
        }
        LOG.info("Storing Counterfactual Explainability result for execution {}", executionId);
        storage.put(solutionId, result);

        LOG.info("Purging Counterfactual Explainability results storage for Counterfactual {}", counterfactualId);
        explainabilityResultsManager.purge(counterfactualId, storage);
    }

    private CounterfactualExplainabilityResult.Stage stageFrom(CounterfactualExplainabilityResultDto.Stage stage) {
        return stage.equals(CounterfactualExplainabilityResultDto.Stage.FINAL) ? CounterfactualExplainabilityResult.Stage.FINAL : CounterfactualExplainabilityResult.Stage.INTERMEDIATE;
    }

}
