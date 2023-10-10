/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.explainability.handlers;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.local.counterfactual.CounterfactualExplainer;

import io.smallrye.context.SmallRyeManagedExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CounterfactualExplainerProducerTest {

    @Test
    void produce() {
        final ManagedExecutor executor = SmallRyeManagedExecutor.builder().build();
        CounterfactualExplainerProducer producer = new CounterfactualExplainerProducer(0.01, executor);
        CounterfactualExplainer counterfactualExplainer = producer.produce();

        assertNotNull(counterfactualExplainer);
        assertEquals(0.01, counterfactualExplainer.getCounterfactualConfig().getGoalThreshold());
        assertEquals(executor, counterfactualExplainer.getCounterfactualConfig().getExecutor());
    }

}
