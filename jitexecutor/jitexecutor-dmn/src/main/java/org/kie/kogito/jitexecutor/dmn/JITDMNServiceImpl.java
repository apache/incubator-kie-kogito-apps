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

package org.kie.kogito.jitexecutor.dmn;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.kie.api.io.Resource;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.internal.utils.DMNRuntimeBuilder;
import org.kie.dmn.core.internal.utils.DynamicDMNContextBuilder;
import org.kie.internal.io.ResourceFactory;
import org.kie.kogito.dmn.rest.DMNResult;
import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.PerturbationContext;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Saliency;

@ApplicationScoped
public class JITDMNServiceImpl implements JITDMNService {

    @Override
    public org.kie.kogito.dmn.rest.DMNResult evaluateModel(String modelXML, Map<String, Object> context) {
        Resource modelResource = ResourceFactory.newReaderResource(new StringReader(modelXML), "UTF-8");
        DMNRuntime dmnRuntime = DMNRuntimeBuilder.fromDefaults().buildConfiguration()
                .fromResources(Collections.singletonList(modelResource)).getOrElseThrow(RuntimeException::new);
        DMNModel dmnModel = dmnRuntime.getModels().get(0);
        DMNContext dmnContext = new DynamicDMNContextBuilder(dmnRuntime.newContext(), dmnModel).populateContextWith(context);
        org.kie.dmn.api.core.DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        return new DMNResult(dmnModel.getNamespace(), dmnModel.getName(), dmnResult);
    }

    @Override
    public String evaluateModelAndExplain(String modelXML, Map<String, Object> context) {
        Resource modelResource = ResourceFactory.newReaderResource(new StringReader(modelXML), "UTF-8");
        DMNRuntime dmnRuntime = DMNRuntimeBuilder.fromDefaults().buildConfiguration()
                .fromResources(Collections.singletonList(modelResource)).getOrElseThrow(RuntimeException::new);
        DMNModel dmnModel = dmnRuntime.getModels().get(0);

        List<Feature> features = new ArrayList<>();
        features.add(FeatureFactory.newCompositeFeature("context", context));
        PredictionInput predictionInput = new PredictionInput(features);

        LocalDMNPredictionProvider localDMNPredictionProvider = new LocalDMNPredictionProvider(dmnModel, dmnRuntime);

        PredictionOutput predictionOutput = null;
        try {
            predictionOutput = localDMNPredictionProvider
                    .predictAsync(Collections.singletonList(predictionInput))
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit())
                    .get(0);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        Prediction prediction = new Prediction(predictionInput, predictionOutput);

        LimeConfig limeConfig = new LimeConfig().withSamples(300).withPerturbationContext(new PerturbationContext(new Random(), 1));
        LimeExplainer limeExplainer = new LimeExplainer(limeConfig);
        Map<String, Saliency> saliencyMap = null;
        try {
            saliencyMap = limeExplainer.explainAsync(prediction, localDMNPredictionProvider)
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
        }

        String response = "";
        for (Saliency saliency : saliencyMap.values()) {
            System.out.println(saliency.getOutput().getName());
            response = saliency.getPerFeatureImportance().stream().map(f -> f.getFeature().getName() + " " + f.getScore()).collect(Collectors.joining("\n"));
            System.out.println(response);
        }
        return saliencyMap.toString();
    }
}
