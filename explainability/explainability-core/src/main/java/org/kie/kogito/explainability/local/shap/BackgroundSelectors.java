package org.kie.kogito.explainability.local.shap;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.PredictionInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;


public class BackgroundSelectors {
    public static List<PredictionInput> randomSelector(List<PredictionInput> pis, int n){
        List<Integer> idx = IntStream.range(0, pis.size()).boxed().collect(Collectors.toList());
        Collections.shuffle(idx);
        return IntStream.range(0, n).mapToObj(i-> pis.get(idx.get(i))).collect(Collectors.toList());
    }

    public static List<PredictionInput> kmeansSelector(List<PredictionInput> pis, int n){
        PredictionInput prototypePI = pis.get(0);
        List<DoublePoint> datapoints = pis.stream().map(pi -> new DoublePoint(pi.getFeatures().stream()
                        .mapToDouble(f -> f.getValue().asNumber()).toArray()))
                .collect(Collectors.toList());
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(n);
        List<CentroidCluster<DoublePoint>> clusters = clusterer.cluster(datapoints);
        List<PredictionInput> background = new ArrayList<>();
        for (CentroidCluster c : clusters){
            double[] center = c.getCenter().getPoint();
            List<Feature> newFeatures = new ArrayList<>();
            for (int i = 0; i<center.length; i++){
                Feature f = prototypePI.getFeatures().get(i);
                if (f.getType() != Type.NUMBER){
                    throw new IllegalArgumentException("KMeans Background Selection Can Only Be Called On Numeric Features");
                }
                newFeatures.add(new Feature(f.getName(), f.getType(), new Value(center[i])));
            }
            background.add(new PredictionInput(newFeatures));
        }
        return background;
    }

    public static List<PredictionInput> cfSelector(List<PredictionInput> pis, int n, PredictionOutput target){

    }

}
