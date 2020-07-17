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
package org.kie.kogito.explainability.global.pdp;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.DoubleStream;

import org.kie.kogito.explainability.model.BlackBoxModel;
import org.kie.kogito.explainability.model.PartialDependenceGraph;
import org.kie.kogito.explainability.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PartialDependencePlotExplainerTest {

    @Test
    void testPdpTextClassifier() throws Exception {
        PartialDependencePlotExplainer partialDependencePlotProvider = new PartialDependencePlotExplainer();
        BlackBoxModel modelInfo = TestUtils.getDummyTextClassifier();
        Collection<PartialDependenceGraph> pdps = partialDependencePlotProvider.explain(modelInfo);
        assertNotNull(pdps);
        for (PartialDependenceGraph partialDependenceGraph : pdps) {
            writeAsciiGraph(partialDependenceGraph, new PrintWriter(new File("target/pdp" + partialDependenceGraph.getFeature().getName() + ".txt")));
        }
    }

    private void writeAsciiGraph(PartialDependenceGraph partialDependenceGraph, PrintWriter out) {
        double[] outputs = partialDependenceGraph.getY();
        double max = DoubleStream.of(outputs).max().getAsDouble();
        double min = DoubleStream.of(outputs).min().getAsDouble();
        outputs = Arrays.stream(outputs).map(d -> d * max / min).toArray();
        double curMax = 1 + DoubleStream.of(outputs).max().getAsDouble();
        ;
        int tempIdx = -1;
        for (int k = 0; k < partialDependenceGraph.getX().length; k++) {
            double tempMax = -Integer.MAX_VALUE;
            for (int j = 0; j < outputs.length; j++) {
                double v = outputs[j];
                if ((int) v < (int) curMax && (int) v > (int) tempMax && tempIdx != j) {
                    tempMax = v;
                    tempIdx = j;
                }
            }
            writeDot(partialDependenceGraph, tempIdx, out);
            curMax = tempMax;
        }
        out.flush();
        out.close();
    }

    private void writeDot(PartialDependenceGraph data, int i, PrintWriter out) {
        for (int j = 0; j < data.getX()[i]; j++) {
            out.print(" ");
        }
        out.println("*");
    }
}