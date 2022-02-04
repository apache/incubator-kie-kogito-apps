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
import java.util.List;

import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.types.FloatingPointPrecision;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.PredictionInput;

import static org.junit.jupiter.api.Assertions.*;

class ArrowConvertersTest {
    public VectorSchemaRoot generateVSR(int n) {
        List<Field> fields = new ArrayList<>();

        Field intField = new Field("i", FieldType.nullable(new ArrowType.Int(32, true)), null);
        fields.add(intField);

        Field boolField = new Field("b", FieldType.nullable(new ArrowType.Bool()), null);
        fields.add(boolField);

        Field doubleField = new Field("d", FieldType.nullable(new ArrowType.FloatingPoint(FloatingPointPrecision.DOUBLE)), null);
        fields.add(doubleField);

        RootAllocator sourceRootAlloc = new RootAllocator(Integer.MAX_VALUE);
        Schema sourceSchema = new Schema(fields, null);
        VectorSchemaRoot sourceRoot = VectorSchemaRoot.create(sourceSchema, sourceRootAlloc);

        IntVector inv = (IntVector) sourceRoot.getVector("i");
        BitVector bnv = (BitVector) sourceRoot.getVector("b");
        Float8Vector dnv = (Float8Vector) sourceRoot.getVector("d");
        inv.allocateNew(n);
        bnv.allocateNew(n);
        dnv.allocateNew(n);
        for (int i = 0; i < n; i++) {
            inv.set(i, i);
            bnv.set(i, i % 2 == 0 ? 1 : 0);
            dnv.set(i, i / (double) n);
        }
        inv.setValueCount(n);
        bnv.setValueCount(n);
        dnv.setValueCount(n);
        sourceRoot.setRowCount(n);
        return sourceRoot;
    }

    double[][] mat = {
            { 5., 6., 7., -4., 8. },
            { 11., 12., 13., -5., 14. },
            { 0., 0, 1., 4., 2. },
    };

    private List<PredictionInput> createPIFromMatrix(double[][] m) {
        List<PredictionInput> pis = new ArrayList<>();
        int[] shape = new int[] { m.length, m[0].length };
        for (int i = 0; i < shape[0]; i++) {
            List<Feature> fs = new ArrayList<>();
            for (int j = 0; j < shape[1]; j++) {
                fs.add(FeatureFactory.newNumericalFeature("f", m[i][j]));
            }
            pis.add(new PredictionInput(fs));
        }
        return pis;
    }

    //    @Test
    //    public void scratch0() {
    //        VectorSchemaRoot vsr = generateVSR(10);
    //        List<PredictionInput> pis = ArrowConverters.convertVSRtoPI(vsr);
    //        System.out.println(pis);
    //    }
    //
    //    @Test
    //    public void scratch1() {
    //        List<PredictionInput> matPI = createPIFromMatrix(mat);
    //        VectorSchemaRoot vsr = ArrowConverters.convertPItoVSR(matPI);
    //        System.out.println(vsr.contentToTSVString());
    //    }
    //
    //    @Test
    //    public void scratch2() {
    //        List<PredictionInput> matPI = createPIFromMatrix(mat);
    //        VectorSchemaRoot vsr = ArrowConverters.convertPItoVSR(matPI);
    //        RootAllocator allocator = new RootAllocator(Integer.MAX_VALUE);
    //        ArrowConverters.write(vsr, allocator);
    //    }
}
