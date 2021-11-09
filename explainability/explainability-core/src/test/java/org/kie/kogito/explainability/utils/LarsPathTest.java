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
package org.kie.kogito.explainability.utils;

import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class LarsPathTest {

    // Check lars_path equality for the first set of input and observations
    RealMatrix X = MatrixUtils.createRealMatrix(new double[][] { { 0.92966881, 0.17435502, 0.86274567, 0.02096693, 0.61729408, 0.27663037, 0.07324771, 0.86299396, 0.20387837, 0.2678897, },
            { 0.46124402, 0.21212798, 0.54547663, 0.85310364, 0.23584478, 0.89939373, 0.90052444, 0.48947526, 0.97695481, 0.31682039 },
            { 0.66084177, 0.54153099, 0.76965712, 0.08213559, 0.9262654, 0.68282777, 0.500637, 0.76781516, 0.14606141, 0.53844816 },
            { 0.44602165, 0.72739983, 0.66221962, 0.20234917, 0.80836334, 0.37038587, 0.67539221, 0.77099063, 0.92992129, 0.56789747 },
            { 0.67568569, 0.37884472, 0.18745406, 0.04757457, 0.09661771, 0.50471931, 0.35367252, 0.75794935, 0.6424804, 0.55250168 },
            { 0.19722479, 0.32117211, 0.70339706, 0.53906674, 0.76903061, 0.32923893, 0.50025901, 0.20776133, 0.1088789, 0.79303772 },
            { 0.31128645, 0.05883037, 0.64210569, 0.88726458, 0.19756748, 0.02448866, 0.2172705, 0.27894779, 0.55028519, 0.70483099 },
            { 0.47339132, 0.14034869, 0.0816702, 0.06699631, 0.06823621, 0.03639515, 0.07545303, 0.1208853, 0.72845905, 0.74802801 },
            { 0.99628077, 0.83760513, 0.63542635, 0.07380346, 0.79007766, 0.55288944, 0.44548098, 0.4055312, 0.70605767, 0.83153303 },
            { 0.47161946, 0.97424448, 0.91217761, 0.6264732, 0.43486423, 0.39281956, 0.66218207, 0.01484187, 0.75595905, 0.04462323 },
    });
    RealVector y = MatrixUtils.createRealVector(new double[] { 6.38923853, -2.16396995, 7.37162403, 1.79236199, 4.21888433, 0.41875855, -3.69136276, -0.50760573, 4.89875242, -4.03316984 });
    List<Integer> correctActives = List.of(7, 3, 0, 4, 8, 5, 9, 2, 1, 6);
    RealVector correctAlphas = MatrixUtils.createRealVector(new double[] { 1.56169836, 0.83963397, 0.68991047, 0.6664122, 0.29931992,
            0.14315316, 0.1200302, 0.00776273, 0.00389666, 0.00187156,
            0. });

    @ParameterizedTest
    @ValueSource(ints = { 2, 3, 4, 5, 6, 7, 8, 9, 10 })
    void testLars10(int maxIter) {
        LarsPathResults lpr = LarsPath.fit(X, y, maxIter, false);
        assertEquals(correctActives.subList(0, maxIter), lpr.getActive().subList(0, maxIter));
        assertArrayEquals(
                correctAlphas.getSubVector(0, maxIter).toArray(),
                lpr.getAlphas().getSubVector(0, maxIter).toArray(),
                1e-6);
    }

    // Check lars_path equality for the second set of input and observations
    RealMatrix X2 = MatrixUtils.createRealMatrix(new double[][] { { 0, 1, 0, 1, 0 },
            { 0, 1, 0, 1, 0 },
            { 0, 1, 1, 0, 1 },
            { 1, 1, 0, 0, 1 },
            { 0, 1, 1, 0, 1 },
            { 0, 0, 1, 0, 1 },
            { 0, 0, 1, 1, 1 },
            { 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0 },
            { 1, 1, 0, 1, 0 },
            { 0, 0, 1, 0, 1 },
            { 1, 0, 0, 0, 0 },
            { 0, 1, 1, 0, 0 },
            { 0, 0, 0, 0, 0 },
            { 0, 1, 1, 1, 1 },
    });
    RealVector y2 = MatrixUtils
            .createRealVector(
                    new double[] { 1.09622926, 1.09622926, 1.07290478, 0.5044599, 1.07290478, 0.88775703, 1.79883855, 0., 0., 1.39511415, 0.88775703, 0.29888489, 1.0524775, 0., 1.98398629 });
    List<Integer> correctActives2 = List.of(1, 2, 3, 4, 0);
    RealVector correctAlphas2 = MatrixUtils.createRealVector(new double[] { 0.61828706, 0.54926307, 0.36443261, 0.183918, 0.04809642, 0. });

    @ParameterizedTest
    @ValueSource(ints = { 5 })
    void testLars5(int maxIter) {
        LarsPathResults lpr = LarsPath.fit(X2, y2, maxIter, false);
        assertEquals(correctActives2.subList(0, maxIter), lpr.getActive().subList(0, maxIter));
        assertArrayEquals(
                correctAlphas2.getSubVector(0, maxIter).toArray(),
                lpr.getAlphas().getSubVector(0, maxIter).toArray(),
                1e-6);
    }

}
