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

package org.kie.kogito.explainability.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.types.pojo.Schema;
import org.kie.kogito.explainability.utils.ArrowConverters;

// wrap a PredictionProviderArrow into a PredictionProvider
// this should perform equivalently to a normal PredictionProvider, provided that it is initialized correctly
public class PPAWrapper implements PredictionProvider {
    PredictionProviderArrow ppa;
    RootAllocator allocator;
    Schema inputSchema;

    public PPAWrapper(PredictionProviderArrow ppa, PredictionInput prototype) {
        this.ppa = ppa;
        this.allocator = new RootAllocator(Integer.MAX_VALUE);
        this.inputSchema = ArrowConverters.generatePrototypePISchema(prototype);
    }

    public RootAllocator getAllocator() {
        return this.allocator;
    }

    @Override
    public CompletableFuture<List<PredictionOutput>> predictAsync(List<PredictionInput> inputs) {
        CompletableFuture<byte[]> outBuf = CompletableFuture.supplyAsync(() -> ArrowConverters.write(ArrowConverters.convertPItoVSR(inputs, this.inputSchema, this.allocator)));
        CompletableFuture<byte[]> resultBuffer = outBuf.thenCompose(ob -> this.ppa.predictAsync(ob));
        return resultBuffer.thenApply(rb -> ArrowConverters.read(rb, this.getAllocator()));
    }
}
