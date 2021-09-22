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
package org.kie.kogito.trusty.service.postgresql.messaging.incoming;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.kie.kogito.cloudevents.CloudEventUtils;
import org.kie.kogito.trusty.service.common.TrustyService;
import org.kie.kogito.trusty.service.common.handlers.ExplainerServiceHandlerRegistry;
import org.kie.kogito.trusty.service.common.messaging.incoming.ExplainabilityResultConsumer;
import org.kie.kogito.trusty.storage.api.StorageExceptionsProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.arc.properties.IfBuildProperty;
import io.smallrye.common.annotation.Blocking;

@ApplicationScoped
@IfBuildProperty(name = "kogito.trusty.blocking", stringValue = "true")
public class BlockingExplainabilityResultConsumer extends ExplainabilityResultConsumer {

    protected BlockingExplainabilityResultConsumer() {
        //CDI proxy
    }

    @Inject
    public BlockingExplainabilityResultConsumer(TrustyService service, ExplainerServiceHandlerRegistry explainerServiceHandlerRegistry, ObjectMapper mapper,
            StorageExceptionsProvider storageExceptionsProvider) {
        super(service, explainerServiceHandlerRegistry, mapper, storageExceptionsProvider);
    }

    @Override
    @Blocking
    @Incoming("trusty-explainability-result")
    public CompletionStage<Void> handleMessage(final Message<String> message) {
        return super.handleMessage(message);
    }

    @Override
    protected void internalHandleMessage(Message<String> message) {
        CompletableFuture.runAsync(() -> CloudEventUtils.decode(message.getPayload()).ifPresent(this::handleCloudEvent)).thenRun(message::ack);
    }
}
