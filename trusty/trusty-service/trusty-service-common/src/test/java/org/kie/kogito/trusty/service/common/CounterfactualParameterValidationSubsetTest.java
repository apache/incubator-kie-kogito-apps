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
package org.kie.kogito.trusty.service.common;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.kie.kogito.tracing.typedvalue.StructureValue;
import org.kie.kogito.tracing.typedvalue.UnitValue;

import com.fasterxml.jackson.databind.node.IntNode;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.trusty.service.common.CounterfactualParameterValidation.isStructureSubset;
import static org.kie.kogito.trusty.service.common.TypedValueTestUtils.buildGoalStructure;
import static org.kie.kogito.trusty.service.common.TypedValueTestUtils.buildGoalUnit;
import static org.kie.kogito.trusty.service.common.TypedValueTestUtils.buildOutcomeStructure;
import static org.kie.kogito.trusty.service.common.TypedValueTestUtils.buildOutcomeUnit;

public class CounterfactualParameterValidationSubsetTest {

    @Test
    public void testGoals_NullNull() {
        assertTrue(isStructureSubset(
                null,
                null));
    }

    @Test
    public void testGoals_EmptyEmpty() {
        assertTrue(isStructureSubset(
                Collections.emptyList(),
                Collections.emptyList()));
    }

    @Test
    public void testGoals_NullEmpty() {
        assertFalse(isStructureSubset(
                null,
                Collections.emptyList()));
    }

    @Test
    public void testGoals_EmptyNull() {
        assertFalse(isStructureSubset(
                Collections.emptyList(),
                null));
    }

    @Test
    public void testGoals_UnitNull() {
        assertFalse(isStructureSubset(
                List.of(buildOutcomeUnit("age", "integer", new IntNode(18))),
                null));
    }

    @Test
    public void testGoals_UnitEmpty() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeUnit("age", "integer", new IntNode(18))),
                Collections.emptyList()));
    }

    @Test
    public void testGoals_NullUnit() {
        assertFalse(isStructureSubset(
                null,
                List.of(buildGoalUnit("age", "integer", new IntNode(18)))));
    }

    @Test
    public void testGoals_EmptyUnit() {
        assertFalse(isStructureSubset(
                Collections.emptyList(),
                List.of(buildGoalUnit("age", "integer", new IntNode(18)))));
    }

    @Test
    public void testGoals_UnitUnit() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeUnit("age", "integer", new IntNode(18))),
                List.of(buildGoalUnit("age", "integer", new IntNode(18)))));
    }

    @Test
    public void testGoals_UnitUnits() {
        assertFalse(isStructureSubset(
                List.of(buildOutcomeUnit("age", "integer", new IntNode(18))),
                List.of(buildGoalUnit("age", "integer", new IntNode(18)),
                        buildGoalUnit("salary", "integer", new IntNode(5000)))));
    }

    @Test
    public void testGoals_UnitsUnit() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeUnit("age", "integer", new IntNode(18)),
                        buildOutcomeUnit("salary", "integer", new IntNode(10000))),
                List.of(buildGoalUnit("age", "integer", new IntNode(18)))));
    }

    @Test
    public void testGoals_UnitsUnits() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeUnit("age", "integer", new IntNode(18)),
                        buildOutcomeUnit("salary", "integer", new IntNode(10000))),
                List.of(buildGoalUnit("age", "integer", new IntNode(18)),
                        buildGoalUnit("salary", "integer", new IntNode(100000)))));
    }

    @Test
    public void testGoals_UnitsUnits__WithDifferentOrder() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeUnit("salary", "integer", new IntNode(10000)),
                        buildOutcomeUnit("age", "integer", new IntNode(18))),
                List.of(buildGoalUnit("age", "integer", new IntNode(18)),
                        buildGoalUnit("salary", "integer", new IntNode(100000)))));
    }

    @Test
    public void testGoals_StructureUnit() {
        assertFalse(isStructureSubset(
                List.of(buildOutcomeStructure("person", "tPerson",
                        Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                "salary", new UnitValue("integer", "integer", new IntNode(10000))))),
                List.of(buildGoalUnit("age", "integer", new IntNode(18)))));
    }

    @Test
    public void testGoals_UnitStructure() {
        assertFalse(isStructureSubset(
                List.of(buildOutcomeUnit("age", "integer", new IntNode(18))),
                List.of(buildGoalStructure("person", "tPerson",
                        Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                "salary", new UnitValue("integer", "integer", new IntNode(100000)))))));
    }

    @Test
    public void testGoals_StructureStructure() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeStructure("person", "tPerson",
                        Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                "salary", new UnitValue("integer", "integer", new IntNode(10000))))),
                List.of(buildGoalStructure("person", "tPerson",
                        Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                "salary", new UnitValue("integer", "integer", new IntNode(100000)))))));
    }

    @Test
    public void testGoals_StructureStructureSubset() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeStructure("person", "tPerson",
                        Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                "salary", new UnitValue("integer", "integer", new IntNode(10000))))),
                List.of(buildGoalStructure("person", "tPerson",
                        Map.of("age", new UnitValue("integer", "integer", new IntNode(18)))))));
    }

    @Test
    public void testGoals_StructureStructure__WithDifferentOrder() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeStructure("person", "tPerson",
                        Map.of("salary", new UnitValue("integer", "integer", new IntNode(10000)),
                                "age", new UnitValue("integer", "integer", new IntNode(18))))),
                List.of(buildGoalStructure("person", "tPerson",
                        Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                "salary", new UnitValue("integer", "integer", new IntNode(100000)))))));
    }

    @Test
    public void testGoals_StructureWithStructureStructureWithStructure() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeStructure("person", "tPerson",
                        Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                "income", new StructureValue("tIncome",
                                        Map.of("salary", new UnitValue("integer", "integer", new IntNode(10000)),
                                                "bonuses", new UnitValue("integer", "integer", new IntNode(50000))))))),
                List.of(buildGoalStructure("person", "tPerson",
                        Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                "income", new StructureValue("tIncome",
                                        Map.of("salary", new UnitValue("integer", "integer", new IntNode(100000)),
                                                "bonuses", new UnitValue("integer", "integer", new IntNode(500000)))))))));
    }

    @Test
    public void testGoals_ComplexComplex() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeUnit("hatSize", "integer", new IntNode(16)),
                        buildOutcomeStructure("person", "tPerson",
                                Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                        "income", new StructureValue("tIncome",
                                                Map.of("salary", new UnitValue("integer", "integer", new IntNode(10000)),
                                                        "bonuses", new UnitValue("integer", "integer", new IntNode(50000))))))),
                List.of(buildGoalUnit("hatSize", "integer", new IntNode(12)),
                        buildGoalStructure("person", "tPerson",
                                Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                        "income", new StructureValue("tIncome",
                                                Map.of("salary", new UnitValue("integer", "integer", new IntNode(100000)),
                                                        "bonuses", new UnitValue("integer", "integer", new IntNode(500000)))))))));
    }

    @Test
    public void testGoals_ComplexComplex__WithDifferentOrder() {
        assertTrue(isStructureSubset(
                List.of(buildOutcomeStructure("person", "tPerson",
                        Map.of("income", new StructureValue("tIncome",
                                Map.of("salary", new UnitValue("integer", "integer", new IntNode(10000)),
                                        "bonuses", new UnitValue("integer", "integer", new IntNode(50000)))),
                                "age", new UnitValue("integer", "integer", new IntNode(18)))),
                        buildOutcomeUnit("hatSize", "integer", new IntNode(16))),
                List.of(buildGoalUnit("hatSize", "integer", new IntNode(12)),
                        buildGoalStructure("person", "tPerson",
                                Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                        "income", new StructureValue("tIncome",
                                                Map.of("salary", new UnitValue("integer", "integer", new IntNode(100000)),
                                                        "bonuses", new UnitValue("integer", "integer", new IntNode(500000)))))))));
    }

    @Test
    public void testGoals_ComplexComplex__WithDifferentOrder_WithDifference() {
        assertFalse(isStructureSubset(
                List.of(buildOutcomeStructure("person", "tPerson",
                        Map.of("income", new StructureValue("tIncome",
                                Map.of("salary", new UnitValue("integer", "integer", new IntNode(10000)))),
                                "age", new UnitValue("integer", "integer", new IntNode(18)))),
                        buildOutcomeUnit("hatSize", "integer", new IntNode(16))),
                List.of(buildGoalUnit("hatSize", "integer", new IntNode(12)),
                        buildGoalStructure("person", "tPerson",
                                Map.of("age", new UnitValue("integer", "integer", new IntNode(18)),
                                        "income", new StructureValue("tIncome",
                                                Map.of("salary", new UnitValue("integer", "integer", new IntNode(100000)),
                                                        "bonuses", new UnitValue("integer", "integer", new IntNode(500000)))))))));
    }
}
