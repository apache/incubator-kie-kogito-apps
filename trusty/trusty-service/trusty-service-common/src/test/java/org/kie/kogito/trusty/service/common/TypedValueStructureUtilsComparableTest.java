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
package org.kie.kogito.trusty.service.common;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.storage.api.model.TypedVariableWithValue;

import com.fasterxml.jackson.databind.node.IntNode;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.trusty.service.common.TypedValueStructureUtils.isStructureComparable;

public class TypedValueStructureUtilsComparableTest {

    @Test
    public void testGoals_NullNull() {
        assertTrue(isStructureComparable(null, null));
    }

    @Test
    public void testGoals_EmptyEmpty() {
        assertTrue(isStructureComparable(Collections.emptyList(), Collections.emptyList()));
    }

    @Test
    public void testGoals_NullEmpty() {
        assertFalse(isStructureComparable(null, Collections.emptyList()));
    }

    @Test
    public void testGoals_EmptyNull() {
        assertFalse(isStructureComparable(Collections.emptyList(), null));
    }

    @Test
    public void testGoals_UnitNull() {
        assertFalse(isStructureComparable(List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18))), null));
    }

    @Test
    public void testGoals_UnitEmpty() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18))),
                Collections.emptyList()));
    }

    @Test
    public void testGoals_NullUnit() {
        assertFalse(isStructureComparable(null,
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)))));
    }

    @Test
    public void testGoals_EmptyUnit() {
        assertFalse(isStructureComparable(Collections.emptyList(),
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)))));
    }

    @Test
    public void testGoals_UnitUnit() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18))),
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)))));
    }

    @Test
    public void testGoals_UnitUnits() {
        assertFalse(isStructureComparable(List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18))),
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                        TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(5000)))));
    }

    @Test
    public void testGoals_UnitsUnit() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000))),
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)))));
    }

    @Test
    public void testGoals_UnitsUnits() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000))),
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                        TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(100000)))));
    }

    @Test
    public void testGoals_UnitsUnits__WithDifferentOrder() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000)),
                TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18))),
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                        TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(100000)))));
    }

    @Test
    public void testGoals_StructureUnit() {
        assertFalse(isStructureComparable(List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                        TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000))))),
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)))));
    }

    @Test
    public void testGoals_UnitStructure() {
        assertFalse(isStructureComparable(List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18))),
                List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                        List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                                TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(100000)))))));
    }

    @Test
    public void testGoals_StructureStructure() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                        TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000))))),
                List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                        List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                                TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(100000)))))));
    }

    @Test
    public void testGoals_StructureStructureSubset() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                        TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000))))),
                List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                        List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)))))));
    }

    @Test
    public void testGoals_StructureStructure__WithDifferentOrder() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                List.of(TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000)),
                        TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18))))),
                List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                        List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                                TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(100000)))))));
    }

    @Test
    public void testGoals_StructureWithStructureStructureWithStructure() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                        TypedVariableWithValue.buildStructure("income", "tIncome",
                                List.of(TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000)),
                                        TypedVariableWithValue.buildUnit("bonuses", "integer", new IntNode(50000))))))),
                List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                        List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                                TypedVariableWithValue.buildStructure("income", "tIncome",
                                        List.of(TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(100000)),
                                                TypedVariableWithValue.buildUnit("bonuses", "integer", new IntNode(500000)))))))));
    }

    @Test
    public void testGoals_ComplexComplex() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildUnit("hatSize", "integer", new IntNode(16)),
                TypedVariableWithValue.buildStructure("person", "tPerson",
                        List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                                TypedVariableWithValue.buildStructure("income", "tIncome",
                                        List.of(TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000)),
                                                TypedVariableWithValue.buildUnit("bonuses", "integer", new IntNode(50000))))))),
                List.of(TypedVariableWithValue.buildUnit("hatSize", "integer", new IntNode(12)),
                        TypedVariableWithValue.buildStructure("person", "tPerson",
                                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                                        TypedVariableWithValue.buildStructure("income", "tIncome",
                                                List.of(TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(100000)),
                                                        TypedVariableWithValue.buildUnit("bonuses", "integer", new IntNode(500000)))))))));
    }

    @Test
    public void testGoals_ComplexComplex__WithDifferentOrder() {
        assertTrue(isStructureComparable(List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                List.of(TypedVariableWithValue.buildStructure("income", "tIncome",
                        List.of(TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000)),
                                TypedVariableWithValue.buildUnit("bonuses", "integer", new IntNode(50000)))),
                        TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)))),
                TypedVariableWithValue.buildUnit("hatSize", "integer", new IntNode(16))),
                List.of(TypedVariableWithValue.buildUnit("hatSize", "integer", new IntNode(12)),
                        TypedVariableWithValue.buildStructure("person", "tPerson",
                                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                                        TypedVariableWithValue.buildStructure("income", "tIncome",
                                                List.of(TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(100000)),
                                                        TypedVariableWithValue.buildUnit("bonuses", "integer", new IntNode(500000)))))))));
    }

    @Test
    public void testGoals_ComplexComplex__WithDifferentOrder_WithDifference() {
        assertFalse(isStructureComparable(List.of(TypedVariableWithValue.buildStructure("person", "tPerson",
                List.of(TypedVariableWithValue.buildStructure("income", "tIncome",
                        List.of(TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(10000)))),
                        TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)))),
                TypedVariableWithValue.buildUnit("hatSize", "integer", new IntNode(16))),
                List.of(TypedVariableWithValue.buildUnit("hatSize", "integer", new IntNode(12)),
                        TypedVariableWithValue.buildStructure("person", "tPerson",
                                List.of(TypedVariableWithValue.buildUnit("age", "integer", new IntNode(18)),
                                        TypedVariableWithValue.buildStructure("income", "tIncome",
                                                List.of(TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(100000)),
                                                        TypedVariableWithValue.buildUnit("bonuses", "integer", new IntNode(500000)))))))));
    }

}
