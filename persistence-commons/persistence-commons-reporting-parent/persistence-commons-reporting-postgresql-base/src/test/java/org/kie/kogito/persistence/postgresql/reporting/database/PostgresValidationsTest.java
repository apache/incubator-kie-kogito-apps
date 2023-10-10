package org.kie.kogito.persistence.postgresql.reporting.database;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.postgresql.reporting.model.JsonType;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresJsonField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMapping;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresPartitionField;
import org.kie.kogito.persistence.reporting.database.Validations;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PostgresValidationsTest {

    @Test
    void testValidateSourceTableIdentityFieldsBlankFieldName() {
        final PostgresField field = new PostgresField("");
        final List<PostgresField> fields = List.of(field);
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTableIdentityFields(fields));
    }

    @Test
    void testValidateSourceTablePartitionFieldsBlankFieldName() {
        final PostgresPartitionField field = new PostgresPartitionField("", "value");
        final List<PostgresPartitionField> fields = List.of(field);
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTablePartitionFields(fields));
    }

    @Test
    void testValidateSourceTablePartitionFieldsBlankFieldValue() {
        final PostgresPartitionField field = new PostgresPartitionField("field", "");
        final List<PostgresPartitionField> fields = List.of(field);
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateSourceTablePartitionFields(fields));
    }

    @Test
    void testValidateTargetTableFieldsBlankSourceJsonPath() {
        final PostgresMapping mapping = new PostgresMapping("",
                new PostgresJsonField("field", JsonType.STRING));
        final List<PostgresMapping> mappings = List.of(mapping);
        assertThrows(IllegalArgumentException.class,
                () -> Validations.validateFieldMappings(mappings));
    }

}
