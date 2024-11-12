/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.jitexecutor.dmn;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNRuntime;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.kie.kogito.jitexecutor.dmn.TestingUtils.getModelFromIoUtils;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DMNEvaluatorTest {

    private static String model;
    private static String invalidModel;
    private static DMNRuntime dmnRuntime;
    private static DMNModel dmnModel;

    @BeforeAll
    public static void setup() throws IOException {
        model = getModelFromIoUtils("invalid_models/DMNv1_x/test.dmn");
        invalidModel = getModelFromIoUtils("invalid_models/DMNv1_5/DMN-Invalid.dmn");
        dmnRuntime = mock(DMNRuntime.class);
        dmnModel = mock(DMNModel.class);
    }

    @Test
    void testFromXMLSuccessModel() {
        String modelXML = model;
        when(dmnRuntime.getModels()).thenReturn(Collections.singletonList(dmnModel));

        DMNEvaluator evaluator = DMNEvaluator.fromXML(modelXML);
        assertNotNull(evaluator);

    }

    @Test
    public void testFromXMLModelWithError() {
        String modelXML = invalidModel;
        when(dmnRuntime.getModels()).thenReturn(Collections.singletonList(dmnModel));

        assertThrows(IllegalStateException.class, () -> {
            DMNEvaluator.fromXML(modelXML);
        });
    }

}
