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

import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLimeOptimizationService implements LimeOptimizationService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultLimeOptimizationService.class);

    private final ExecutorService executor;
    private final BlockingQueue<Future<LimeOptimizationResponse>> queue;
    private final WeakHashMap<LimeExplainer, LimeConfig> register;
    private final LimeConfigOptimizer limeConfigOptimizer;

    public DefaultLimeOptimizationService(ExecutorService executor, LimeConfigOptimizer limeConfigOptimizer) {
        this.executor = executor;
        this.limeConfigOptimizer = limeConfigOptimizer;
        register = new WeakHashMap<>();
        this.queue = new LinkedBlockingDeque<>();
        this.executor.submit(() -> {
            try {
                Future<LimeOptimizationResponse> future;
                while ((future = queue.take()) != null) { // blocking call
                    LimeOptimizationResponse limeOptimizationResponse = future.get();
                    register.put(limeOptimizationResponse.getExplainer(), limeOptimizationResponse.getLimeConfig());
                    logger.info("optimized config registered");
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error("could not complete task ... {}", e.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean submit(LimeOptimizationRequest limeOptimizationRequest) {
        Future<LimeOptimizationResponse> submittedJob = executor.submit(() -> {
            LimeConfig optimizedConfig = limeConfigOptimizer.optimize(limeOptimizationRequest.getLimeConfig(),
                    limeOptimizationRequest.getPredictions(),
                    limeOptimizationRequest.getPredictionProvider());
            return new LimeOptimizationResponse(limeOptimizationRequest.getExplainer(), optimizedConfig);
        });
        return queue.add(submittedJob);
    }

    @Override
    public LimeConfig getBestConfigFor(LimeExplainer limeExplainer) {
        return register.get(limeExplainer);
    }
}
