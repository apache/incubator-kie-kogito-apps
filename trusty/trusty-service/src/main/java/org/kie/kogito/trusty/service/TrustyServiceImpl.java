/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.trusty.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.explainability.api.ExplainabilityRequestDto;
import org.kie.kogito.explainability.api.ModelIdentifierDto;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.AttributeFilter;
import org.kie.kogito.persistence.api.query.QueryFilterFactory;
import org.kie.kogito.tracing.typedvalue.TypedValue;
import org.kie.kogito.trusty.service.messaging.incoming.ModelIdCreator;
import org.kie.kogito.trusty.service.messaging.outgoing.ExplainabilityRequestProducer;
import org.kie.kogito.trusty.storage.api.TrustyStorageService;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.api.model.Execution;
import org.kie.kogito.trusty.storage.api.model.ExplainabilityResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.kogito.trusty.service.messaging.MessagingUtils.modelToTracingTypedValue;

@ApplicationScoped
public class TrustyServiceImpl implements TrustyService {

    private static final Logger LOG = LoggerFactory.getLogger(TrustyServiceImpl.class);

    @ConfigProperty(name = "trusty.explainability.enabled")
    Boolean isExplainabilityEnabled;

    @Inject
    ExplainabilityRequestProducer explainabilityRequestProducer;

    @Inject
    TrustyStorageService storageService;

    TrustyServiceImpl() {
        // dummy constructor needed
    }

    public TrustyServiceImpl(TrustyStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public List<Execution> getExecutionHeaders(OffsetDateTime from, OffsetDateTime to, int limit, int offset, String prefix) {
        Storage<String, Decision> storage = storageService.getDecisionsStorage();
        List<AttributeFilter<?>> filters = new ArrayList<>();
        filters.add(QueryFilterFactory.like(Execution.EXECUTION_ID_FIELD, prefix + "*"));
        filters.add(QueryFilterFactory.greaterThanEqual(Execution.EXECUTION_TIMESTAMP_FIELD, from.toInstant().toEpochMilli()));
        filters.add(QueryFilterFactory.lessThanEqual(Execution.EXECUTION_TIMESTAMP_FIELD, to.toInstant().toEpochMilli()));
        return new ArrayList<>(storage.query().limit(limit).offset(offset).filter(filters).execute());
    }

    @Override
    public Decision getDecisionById(String executionId) {
        Storage<String, Decision> storage = storageService.getDecisionsStorage();
        if (!storage.containsKey(executionId)) {
            throw new IllegalArgumentException(String.format("A decision with ID %s does not exist in the storage.", executionId));
        }
        return storage.get(executionId);
    }

    @Override
    public void storeDecision(String executionId, Decision decision) {
        Storage<String, Decision> storage = storageService.getDecisionsStorage();
        if (storage.containsKey(executionId)) {
            throw new IllegalArgumentException(String.format("A decision with ID %s is already present in the storage.", executionId));
        }
        storage.put(executionId, decision);
    }

    @Override
    public void updateDecision(String executionId, Decision decision) {
        storageService.getDecisionsStorage().put(executionId, decision);
    }

    @Override
    public void processDecision(String executionId, String serviceUrl, Decision decision) {
        storeDecision(executionId, decision);
        // TODO: Create a proper ExplainabilityRequestDto when all the properties will be defined and available. https://issues.redhat.com/browse/KOGITO-2944
        if (Boolean.TRUE.equals(isExplainabilityEnabled)) {
            Map<String, TypedValue> inputs = decision.getInputs() != null
                    ? decision.getInputs().stream()
                    .collect(HashMap::new, (m, v) -> m.put(v.getName(), modelToTracingTypedValue(v.getValue())), HashMap::putAll)
                    : Collections.emptyMap();

            Map<String, TypedValue> outputs = decision.getOutcomes() != null
                    ? decision.getOutcomes().stream()
                    .collect(HashMap::new, (m, v) -> m.put(v.getOutcomeName(), modelToTracingTypedValue(v.getOutcomeResult())), HashMap::putAll)
                    : Collections.emptyMap();

            explainabilityRequestProducer.sendEvent(new ExplainabilityRequestDto(
                    executionId,
                    serviceUrl,
                    createDecisionModelIdentifierDto(decision),
                    inputs,
                    outputs
            ));
        }
    }

    @Override
    public ExplainabilityResult getExplainabilityResultById(String executionId) {
        Storage<String, ExplainabilityResult> storage = storageService.getExplainabilityResultStorage();
        if (!storage.containsKey(executionId)) {
            throw new IllegalArgumentException(String.format("A explainability result with ID %s does not exist in the storage.", executionId));
        }
        return storage.get(executionId);
    }

    @Override
    public void storeExplainabilityResult(String executionId, ExplainabilityResult result) {
        Storage<String, ExplainabilityResult> storage = storageService.getExplainabilityResultStorage();
        if (storage.containsKey(executionId)) {
            throw new IllegalArgumentException(String.format("A explainability result with ID %s is already present in the storage.", executionId));
        }
        storage.put(executionId, result);
        LOG.info("Stored explainability result for execution {}", executionId);
    }

    @Override
    public void storeModel(String groupId, String artifactId, String version, String name, String namespace, String definition) {
        final String identifier = ModelIdCreator.makeIdentifier(groupId, artifactId, version, name, namespace);
        final Storage<String, String> storage = storageService.getModelStorage();
        if (storage.containsKey(identifier)) {
            throw new IllegalArgumentException(String.format("A model with ID %s is already present in the storage.", identifier));
        }
        storage.put(identifier, definition);
    }

    @Override
    public String getModelById(String modelId) {
        final Storage<String, String> storage = storageService.getModelStorage();
        if (!storage.containsKey(modelId)) {
            throw new IllegalArgumentException(String.format("A model with ID %s does not exist in the storage.", modelId));
        }
        return storage.get(modelId);
    }

    private ModelIdentifierDto createDecisionModelIdentifierDto(Decision decision) {
        String resourceId = decision.getExecutedModelNamespace() +
                ModelIdentifierDto.RESOURCE_ID_SEPARATOR +
                decision.getExecutedModelName();
        return new ModelIdentifierDto("dmn", resourceId);
    }
}
