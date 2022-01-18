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
package org.kie.kogito.persistence.postgresql.reporting.database;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.postgresql.reporting.model.JsonType;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMapping;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresPartitionField;
import org.kie.kogito.persistence.reporting.database.Validations;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PostgresValidationsTest {

    @Test
    void testValidateSourceTableIdentityFieldsBlankFieldName() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTableIdentityFields(List.of(new PostgresField("", JsonType.STRING))));
    }

    @Test
    void testValidateSourceTablePartitionFieldsBlankFieldName() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTablePartitionFields(List.of(new PostgresPartitionField("", JsonType.STRING, "value"))));
    }

    @Test
    void testValidateSourceTablePartitionFieldsBlankFieldValue() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTablePartitionFields(List.of(new PostgresPartitionField("field", JsonType.STRING, ""))));
    }

    @Test
    void testValidateTargetTableFieldsBlankSourceJsonPath() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateFieldMappings(List.of(new PostgresMapping("",
                        new PostgresField("field", JsonType.STRING)))));
    }

}
