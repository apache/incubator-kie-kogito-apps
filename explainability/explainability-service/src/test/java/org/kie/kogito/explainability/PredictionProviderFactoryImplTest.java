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
package org.kie.kogito.explainability;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.model.PredictionProvider;

import io.vertx.mutiny.core.Vertx;

import static org.kie.kogito.explainability.TestUtils.LIME_REQUEST;

class PredictionProviderFactoryImplTest {

    @Test
    void createPredictionProvider() {
        PredictionProviderFactoryImpl factory = new PredictionProviderFactoryImpl(
                Vertx.vertx(),
                ThreadContext.builder().build(),
                ManagedExecutor.builder().build());
        PredictionProvider predictionProvider = factory.createPredictionProvider(LIME_REQUEST);
        Assertions.assertNotNull(predictionProvider);
        Assertions.assertTrue(predictionProvider instanceof RemotePredictionProvider);
    }
}
