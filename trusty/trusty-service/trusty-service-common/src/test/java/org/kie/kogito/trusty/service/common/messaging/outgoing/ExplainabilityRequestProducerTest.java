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

package org.kie.kogito.trusty.service.common.messaging.outgoing;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.LIMEExplainabilityRequestDto;
import org.kie.kogito.explainability.api.ModelIdentifierDto;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExplainabilityRequestProducerTest {

    @Test
    void test() {
        AssertSubscriber<String> subscriber = AssertSubscriber.create(1);

        ExplainabilityRequestProducer producer = new ExplainabilityRequestProducer();
        producer.getEventPublisher().subscribe(subscriber);

        producer.sendEvent(new LIMEExplainabilityRequestDto(
                "executionId", "http://localhost:8080/model",
                new ModelIdentifierDto("dmn", "modelNamespace:model"),
                Collections.emptyMap(),
                Collections.emptyMap()));

        assertEquals(1, subscriber.getItems().size());
    }
}
