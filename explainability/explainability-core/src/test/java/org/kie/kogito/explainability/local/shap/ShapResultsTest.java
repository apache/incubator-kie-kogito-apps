package org.kie.kogito.explainability.local.shap;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureImportance;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;

class ShapResultsTest {
    ShapResults buildShapResults(int nOutputs, int nFeatures, int scalar1, int scalar2){
        Saliency[] saliencies= new Saliency[nOutputs];
        for (int i=0; i<nOutputs; i++) {
            List<FeatureImportance> fis = new ArrayList<>();
            for (int j = 0; j < nFeatures; j++) {
                fis.add(new FeatureImportance(new Feature("f", Type.NUMBER, new Value(j)), i * j * scalar1));
            }
            saliencies[i] = new Saliency(new Output("o", Type.NUMBER, new Value(i), 1.0), fis);
        }
        RealVector fnull = MatrixUtils.createRealVector(new double[nOutputs]);
        fnull.mapAddToSelf(scalar2);
        return new ShapResults(saliencies, fnull);
    }

    @Test
    void testEqualsSameObj() {
        ShapResults sr1 = buildShapResults(2, 2, 1, 1);
        assertEquals(sr1, sr1);
        assertEquals(sr1.hashCode(), sr1.hashCode());
    }

    @Test
    void testEquals() {
        ShapResults sr1 = buildShapResults(2, 2, 1, 1);
        ShapResults sr2 = buildShapResults(2, 2, 1, 1);
        assertEquals(sr1, sr2);
        assertNotEquals(sr1.hashCode(), sr2.hashCode());
    }

    @Test
    void testDiffOutputs() {
        ShapResults sr1 = buildShapResults(2, 2, 1, 1);
        ShapResults sr2 = buildShapResults(20, 2, 1, 1);
        assertNotEquals(sr1, sr2);
        assertNotEquals(sr1.hashCode(), sr2.hashCode());
    }

    @Test
    void testDiffFeatures() {
        ShapResults sr1 = buildShapResults(2, 2, 1, 1);
        ShapResults sr2 = buildShapResults(2, 20, 1, 1);
        assertNotEquals(sr1, sr2);
        assertNotEquals(sr1.hashCode(), sr2.hashCode());
    }

    @Test
    void testDiffImportances() {
        ShapResults sr1 = buildShapResults(2, 2, 1, 1);
        ShapResults sr2 = buildShapResults(2, 2, 10, 1);
        assertNotEquals(sr1, sr2);
        assertNotEquals(sr1.hashCode(), sr2.hashCode());
    }

    @Test
    void testDiffFnull() {
        ShapResults sr1 = buildShapResults(2, 2, 1, 1);
        ShapResults sr2 = buildShapResults(2, 2, 1, 10);
        assertNotEquals(sr1, sr2);
        assertNotEquals(sr1.hashCode(), sr2.hashCode());
    }
}