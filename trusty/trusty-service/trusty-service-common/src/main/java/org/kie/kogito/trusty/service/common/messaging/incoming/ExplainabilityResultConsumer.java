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

package org.kie.kogito.trusty.service.common.messaging.incoming;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.kie.kogito.explainability.api.BaseExplainabilityResultDto;
import org.kie.kogito.trusty.service.common.TrustyService;
import org.kie.kogito.trusty.service.common.handlers.ExplainerServiceHandlerRegistry;
import org.kie.kogito.trusty.service.common.messaging.BaseEventConsumer;
import org.kie.kogito.trusty.storage.api.model.BaseExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cloudevents.CloudEvent;

@ApplicationScoped
public class ExplainabilityResultConsumer extends BaseEventConsumer<BaseExplainabilityResultDto> {

    private static final Logger LOG = LoggerFactory.getLogger(ExplainabilityResultConsumer.class);
    private static final TypeReference<BaseExplainabilityResultDto> CLOUD_EVENT_TYPE = new TypeReference<>() {
    };

    private ExplainerServiceHandlerRegistry explainerServiceHandlerRegistry;

    private ExplainabilityResultConsumer() {
        //CDI proxy
    }

    @Inject
    public ExplainabilityResultConsumer(TrustyService service,
            ExplainerServiceHandlerRegistry explainerServiceHandlerRegistry,
            ObjectMapper mapper) {
        super(service, mapper);
        this.explainerServiceHandlerRegistry = explainerServiceHandlerRegistry;
    }

    protected <T extends BaseExplainabilityResultDto> BaseExplainabilityResult explainabilityResultFrom(T dto, Decision decision) {
        return explainerServiceHandlerRegistry.explainabilityResultFrom(dto, decision);
    }

    @Override
    @Incoming("trusty-explainability-result")
    public CompletionStage<Void> handleMessage(Message<String> message) {
        return super.handleMessage(message);
    }

    @Override
    protected void internalHandleCloudEvent(CloudEvent cloudEvent, BaseExplainabilityResultDto payload) {
        LOG.debug("CloudEvent received {}", payload);

        String executionId = payload.getExecutionId();
        Decision decision = getDecisionById(executionId);
        if (decision == null) {
            LOG.warn("Can't find decision related to explainability result (executionId={})", executionId);
        }
        service.storeExplainabilityResult(executionId, explainabilityResultFrom(payload, decision));
    }

    @Override
    protected TypeReference<BaseExplainabilityResultDto> getEventType() {
        return CLOUD_EVENT_TYPE;
    }

    protected Decision getDecisionById(String executionId) {
        try {
            return service.getDecisionById(executionId);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
