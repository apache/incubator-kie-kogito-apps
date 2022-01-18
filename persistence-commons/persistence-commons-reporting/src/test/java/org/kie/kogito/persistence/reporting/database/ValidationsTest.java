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
package org.kie.kogito.persistence.reporting.database;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationsTest {

    @Test
    void testValidateMappingIdNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateMappingId(null));
    }

    @Test
    void testValidateMappingIdBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateMappingId(""));
    }

    @Test
    void testValidateMappingId() {
        assertEquals("mappingId",
                Validations.validateMappingId("mappingId"));
    }

    @Test
    void testValidateSourceTableNameNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTableName(null));
    }

    @Test
    void testValidateSourceTableNameBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTableName(""));
    }

    @Test
    void testValidateSourceTableName() {
        assertEquals("validateSourceTableName",
                Validations.validateSourceTableName("validateSourceTableName"));
    }

    @Test
    void testValidateSourceTableJsonFieldNameNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTableJsonFieldName(null));
    }

    @Test
    void testValidateSourceTableJsonFieldNameBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTableJsonFieldName(""));
    }

    @Test
    void testValidateSourceTableJsonFieldName() {
        assertEquals("validateSourceTableJsonFieldName",
                Validations.validateSourceTableJsonFieldName("validateSourceTableJsonFieldName"));
    }

    @Test
    void testValidateSourceTableIdentityFieldsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTableIdentityFields(null));
    }

    @Test
    void testValidateSourceTableIdentityFieldsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTableIdentityFields(Collections.emptyList()));
    }

    @Test
    void testValidateSourceTablePartitionFieldsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTablePartitionFields(null));
    }

    @Test
    void testValidateSourceTablePartitionFieldsEmpty() {
        assertEquals(Collections.emptyList(),
                Validations.validateSourceTablePartitionFields(Collections.emptyList()));
    }

    @Test
    void testValidateTargetTableNameNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateTargetTableName(null));
    }

    @Test
    void testValidateTargetTableNameBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateTargetTableName(""));
    }

    @Test
    void testValidateTargetTableName() {
        assertEquals("targetTableName",
                Validations.validateTargetTableName("targetTableName"));
    }

    @Test
    void testValidateTargetTableFieldsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateFieldMappings(null));
    }

    @Test
    void testValidateTargetTableFieldsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateFieldMappings(Collections.emptyList()));
    }
}
