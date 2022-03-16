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

package org.kie.kogito.explainability.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;

public class OneHotter {
    Map<String, LinkedHashSet<Value>> categoricals = new HashMap<>();

    private void addFeatureValue(Feature f) {
        if (!categoricals.containsKey(f.getName())) {
            this.categoricals.put(f.getName(), new LinkedHashSet<>(List.of(f.getValue())));
        } else {
            LinkedHashSet<Value> currVals = this.categoricals.get(f.getName());
            currVals.add(f.getValue());
            this.categoricals.put(f.getName(), currVals);
        }
    }

    public OneHotter(List<PredictionInput> pis) {
        for (PredictionInput pi : pis) {
            for (Feature f : pi.getFeatures()) {
                if (f.getType() == Type.CATEGORICAL) {
                    addFeatureValue(f);
                }
            }
        }
    }

    // === ENCODERS ====================================================================================================
    public PredictionInput oneHotEncode(PredictionInput pi, boolean proxy) {
        if (categoricals.isEmpty()) {
            return pi;
        }
        List<Feature> encodedFeatures = new ArrayList<>();
        for (Feature f : pi.getFeatures()) {
            if (categoricals.containsKey(f.getName()) && f.getType() == Type.CATEGORICAL) {
                if (!categoricals.get(f.getName()).contains(f.getValue())) {
                    addFeatureValue(f);
                }
                Value[] comparedVals = categoricals.get(f.getName()).toArray(new Value[0]);

                for (int i = 0; i < comparedVals.length; i++) {
                    if (proxy && comparedVals[i].equals(f.getValue())) {
                        encodedFeatures.add(new Feature(
                                f.getName() + "_OHEPROXY",
                                Type.NUMBER,
                                new Value(i)));
                        break;
                    } else if (!proxy) {
                        encodedFeatures.add(new Feature(
                                f.getName() + "_OHE_" + i + "_OHE_" + comparedVals[i],
                                Type.NUMBER,
                                new Value(f.getValue().equals(comparedVals[i]) ? 1 : 0)));

                    }
                }
            } else {
                encodedFeatures.add(f);
            }
        }
        return new PredictionInput(encodedFeatures);
    }

    public List<PredictionInput> oneHotEncode(List<PredictionInput> pis, boolean proxy) {
        if (categoricals.isEmpty()) {
            return pis;
        }
        List<PredictionInput> encodedPIs = new ArrayList<>();
        for (PredictionInput pi : pis) {
            encodedPIs.add(oneHotEncode(pi, proxy));
        }
        return encodedPIs;
    }

    // === DECODERS ====================================================================================================
    public PredictionInput oneHotDecode(PredictionInput pi, boolean proxy) {
        if (categoricals.isEmpty()) {
            return pi;
        }
        String proxyIndicator = proxy ? "_OHEPROXY" : "_OHE_";
        List<Feature> decodedFeatures = new ArrayList<>();
        for (Feature f : pi.getFeatures()) {
            if (f.getName().contains(proxyIndicator)) {
                if (proxy) {
                    String parentFeature = f.getName().split("_OHEPROXY")[0];
                    decodedFeatures.add(new Feature(
                            parentFeature,
                            Type.CATEGORICAL,
                            (Value) categoricals.get(parentFeature).toArray()[(int) f.getValue().asNumber()]));
                } else if (f.getValue().asNumber() == 1) {
                    String[] splitName = f.getName().split("_OHE_");
                    String parentFeature = splitName[0];
                    int categoricalValue = Integer.parseInt(splitName[1]);
                    decodedFeatures.add(new Feature(
                            parentFeature,
                            Type.CATEGORICAL,
                            (Value) categoricals.get(parentFeature).toArray()[categoricalValue]));
                }
            } else {
                decodedFeatures.add(f);
            }
        }
        return new PredictionInput(decodedFeatures);
    }

    public List<PredictionInput> oneHotDecode(List<PredictionInput> pis, boolean proxy) {
        if (categoricals.isEmpty()) {
            return pis;
        }
        List<PredictionInput> decodedPIs = new ArrayList<>();
        for (PredictionInput pi : pis) {
            decodedPIs.add(oneHotDecode(pi, proxy));
        }
        return decodedPIs;
    }
}
