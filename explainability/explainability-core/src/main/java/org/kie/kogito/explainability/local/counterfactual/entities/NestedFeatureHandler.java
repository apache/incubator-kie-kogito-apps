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

package org.kie.kogito.explainability.local.counterfactual.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.utils.DataUtils;

public class NestedFeatureHandler {

    public static List<Feature> flattenFeatures(List<Feature> features) {
        return DataUtils.getLinearizedFeatures(features);
    }

    public static List<Feature> getDelinearizedFeatures(List<Feature> flattenedFeatures, List<Feature> originalFeatures) {
        AtomicInteger tracker = new AtomicInteger();
        List<Feature> nested = new LinkedList<>();
        for (Feature f : originalFeatures) {
            nested.add(delinearizeFeature(flattenedFeatures, tracker, f));
        }
        return nested;
    }

    private static Feature delinearizeFeature(List<Feature> flattenedFeatures, AtomicInteger tracker, Feature f) {
        Feature newf = null;
        if (Type.UNDEFINED.equals(f.getType())) {
            if (f.getValue().getUnderlyingObject() instanceof Feature) {
                newf = FeatureFactory.copyOf(f,
                        new Value(delinearizeFeature(flattenedFeatures, tracker, (Feature) f.getValue().getUnderlyingObject())));
            } else {
                newf = FeatureFactory.copyOf(f, flattenedFeatures.get(tracker.getAndIncrement()).getValue());
            }
        } else if (Type.COMPOSITE.equals(f.getType())) {
            if (f.getValue().getUnderlyingObject() instanceof List) {
                List<Feature> features = (List<Feature>) f.getValue().getUnderlyingObject();
                List<Feature> newfs = new LinkedList<>();
                for (Feature feature : features) {
                    newfs.add(delinearizeFeature(flattenedFeatures, tracker, feature));
                }
                newf = FeatureFactory.newCompositeFeature(f.getName(), newfs);
            } else {
                newf = FeatureFactory.copyOf(f, flattenedFeatures.get(tracker.getAndIncrement()).getValue());
            }
        } else {
            newf = FeatureFactory.copyOf(f, flattenedFeatures.get(tracker.getAndIncrement()).getValue());
        }
        return newf;
    }

}
