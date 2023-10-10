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
package org.kie.kogito.persistence.postgresql.reporting.model;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PostgresMappingDefinitionTest {

    static final PostgresMappingDefinition DEFINITION = new PostgresMappingDefinition("mappingId",
            "sourceTableName",
            "sourceTableJsonFieldName",
            List.of(PostgresFieldTest.FIELD),
            List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
            "targetTableName",
            List.of(PostgresMappingTest.MAPPING));
    static final int DEFINITION_HASHCODE = DEFINITION.hashCode();

    @Test
    void testEquality() {
        assertEquals(DEFINITION,
                DEFINITION);
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("different",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "different",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "different",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD, PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD, PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "different",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING, PostgresMappingTest.MAPPING)));
    }

    @Test
    void testHashCode() {
        assertEquals(DEFINITION_HASHCODE,
                DEFINITION.hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("different",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "different",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "different",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD, PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD, PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "different",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING, PostgresMappingTest.MAPPING)).hashCode());
    }
}
