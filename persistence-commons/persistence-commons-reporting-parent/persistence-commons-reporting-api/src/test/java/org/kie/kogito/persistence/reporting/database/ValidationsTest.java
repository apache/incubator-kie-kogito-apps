package org.kie.kogito.persistence.reporting.database;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.reporting.model.Field;
import org.kie.kogito.persistence.reporting.model.PartitionField;
import org.kie.kogito.persistence.reporting.test.TestTypes.TestMapping;

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
        final List<Field> fields = Collections.emptyList();
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTableIdentityFields(fields));
    }

    @Test
    void testValidateSourceTablePartitionFieldsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTablePartitionFields(null));
    }

    @Test
    void testValidateSourceTablePartitionFieldsEmpty() {
        final List<PartitionField> partitionFields = Collections.emptyList();
        assertEquals(Collections.emptyList(),
                Validations.validateSourceTablePartitionFields(partitionFields));
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
        final List<TestMapping> mappings = Collections.emptyList();
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateFieldMappings(mappings));
    }
}
